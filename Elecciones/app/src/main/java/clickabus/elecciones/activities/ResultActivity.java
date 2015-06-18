package clickabus.elecciones.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.clickabus.elecciones.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import clickabus.elecciones.connection.NetFunctions;
import clickabus.elecciones.database.DataBaseHelper;
import clickabus.elecciones.model.AdministrativeArea;
import clickabus.elecciones.model.Answer;
import clickabus.elecciones.model.Association;
import clickabus.elecciones.model.DialogoSeleccion;
import clickabus.elecciones.model.Party;
import clickabus.elecciones.model.PartyAdminArea;
import clickabus.elecciones.model.Question;
import clickabus.elecciones.model.UserAnswer;

public class ResultActivity extends ActionBarActivity {

    private File picFile;

    private static String net_share = "";
    private static final String SHARE_COMMENT = "Estos han sido mis resultados en el test de @testelecciones. Descarga la app gratis en: https://goo.gl/4NvZPT";

    private int number_of_questions_dinamic = 0;

    private static final String USER_ANSWERS_KEY = "user_answers";
    private static final String ADMIN_AREA = "admin_area";

    private ArrayList<Association> associations = new ArrayList<>();
    private ArrayList<Party> parties = new ArrayList<>();
    private ArrayList<Answer> answers = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<AdministrativeArea> administrativeAreas = new ArrayList<>();
    private ArrayList<PartyAdminArea> partyAdminAreas = new ArrayList<>();

    private AdministrativeArea administrativeArea;

    private HorizontalBarChart barChart;
    // private TextView textView1,textView2,textView3,textView4,textView5,textView6;
    // private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6;
    private RelativeLayout relativeLayout;

    private UploadResult uploadResult;

    private ArrayList<UserAnswer> user_answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_chart);

        relativeLayout = (RelativeLayout)findViewById(R.id.result);
        Drawable background = relativeLayout.getBackground();
        background.setAlpha(80);

        barChart = new HorizontalBarChart(this);

        relativeLayout.addView(barChart);

        DataBaseHelper database = new DataBaseHelper(getApplicationContext());

        // Get the Bundle Object
        Bundle bundleObject = getIntent().getExtras();

        // Get ArrayList Bundle
        user_answers = (ArrayList<UserAnswer>) bundleObject.getSerializable(USER_ANSWERS_KEY);


        administrativeArea = (AdministrativeArea) bundleObject.getSerializable(ADMIN_AREA);

        // Obtengo asociaciones y partidos.
        associations = database.getAssociations();
        parties = database.getParties();
        questions = database.getQuestions();
        answers = database.getAnswers();
        administrativeAreas = database.getAdminAreas();
        partyAdminAreas = database.getPartyAreaAssociations();

        parties = getPartiesInArea();

        // Obtengo máxima puntuación que puede llevarse cada partido.
        getMaxScore2();

        int num_respuestas_contestadas = 0;
        // Recorro array con las respuestas del usuario.
        for(UserAnswer ua : user_answers){
            // Para cada respuesta, obtengo las asociaciones respuesta-partido.
            ArrayList<Association> assoc = getAssociationsByAnswerId(ua.getAnswerId());
            // Para cada asociación, sumo al partido los puntos que le correspondan.
            for(Association a : assoc){
                sumPartyScore(a.getPartyId(), a.getScore());
            }
            num_respuestas_contestadas++;
        }
        number_of_questions_dinamic = num_respuestas_contestadas;
        getPercentageScore();

        Collections.sort(parties, new Comparator<Party>() {
            @Override
            public int compare(Party p1, Party p2) {
                return new Integer(p1.getPartyScore()).compareTo(new Integer(p2.getPartyScore()));
            }
        });

        for(Party p: parties){
            Log.i(p.getPartyName() +": ", String.valueOf(p.getPartyScore()) + "%");
        }

        //drawImages();
        drawChart();

        uploadResult = new UploadResult();
        if(isNetworkAvailable()) uploadResult.execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == R.id.action_back){
            finish();
        }
        if(id == R.id.action_share){
            shareit();

        }
        return super.onOptionsItemSelected(item);
    }


    private ArrayList<Party> getPartiesInArea(){
        ArrayList<Party> partiesInArea = new ArrayList<>();
        for(Party p : parties){
            for(PartyAdminArea pa : partyAdminAreas){
                if(pa.getAreaId()==administrativeArea.getAdminAreaId() && p.getPartyId()==pa.getPartyId()){
                    partiesInArea.add(p);
                    break;
                }
            }
        }
        return partiesInArea;
    }

    /**
     * Obtengo un array de associaciones según el id de la respuesta.
     * @param answer_id: id de la respuesta.
     * @return Array con las asociaciones.
     */
    private ArrayList<Association> getAssociationsByAnswerId(int answer_id){
        ArrayList<Association> assoc = new ArrayList<>();
        for(Association a : associations){
            if(a.getAnswerId()==answer_id){
                assoc.add(a);
            }
        }
        return assoc;
    }

    /**
     * Sumo al partido la puntuación que le corresponda.
     * @param party_id: id del partido al que se le deben subir.
     * @param sum_score: puntuación que se le debe sumar.
     */
    private void sumPartyScore(int party_id, int sum_score){
        for(Party p: parties){
            if(p.getPartyId()==party_id){
                p.setPartyScore(p.getPartyScore()+sum_score);
            }
        }
    }

    private void getMaxScore2(){
        for(Party p : parties){
            for(UserAnswer ua : user_answers){
                for (Question q : questions) {
                    if(ua.getQuestionId()==q.getQuestionId()) {
                        ArrayList<Answer> ans = getAnswersOfQuestion(q);
                        for (Answer a : ans) {
                            ArrayList<Association> assoc = getAssociationsByAnswerId(a.getAnswerId());
                            for (Association as : assoc) {
                                if (as.getPartyId() == p.getPartyId() && as.getScore() == 2) {
                                    p.newAnsweredQuestion();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private void getMaxScore(){
        for(Party p: parties) {
            for (Question q : questions) {
                ArrayList<Answer> ans = getAnswersOfQuestion(q);
                for (Answer a : ans) {
                    ArrayList<Association> assoc = getAssociationsByAnswerId(a.getAnswerId());
                    for (Association as : assoc) {
                        if(as.getPartyId()==p.getPartyId() && as.getScore()==2){
                            p.newAnsweredQuestion();
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Answer> getAnswersOfQuestion(Question q){
        ArrayList<Answer> ans = new ArrayList<>();
        for(Answer a : answers){
            if(a.getQuestionId()==q.getQuestionId()){
                ans.add(a);
            }
        }
        return ans;
    }

    private void getPercentageScore(){
        for(Party p : parties){
            int num_good_answers = (int)(p.getPartyScore()/2);
            Log.i("Preguntas afines:", String.valueOf(num_good_answers));
            int percentage;
            int denominador = number_of_questions_dinamic-(number_of_questions_dinamic-p.getAnsweredQuestion());
            if(denominador==0){
                percentage = 0;
            }else{
                percentage = (int)(num_good_answers*100/(number_of_questions_dinamic-(number_of_questions_dinamic-p.getAnsweredQuestion())));
            }

            p.setPartyScore(percentage);

        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(uploadResult!=null){
            if(uploadResult.getStatus()== AsyncTask.Status.PENDING || uploadResult.getStatus()== AsyncTask.Status.RUNNING){
                uploadResult.cancel(true);
            }}
    }
    private void drawChart(){
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        int i = 0;
        //int f = 3;
        for(Party p : parties){
            //if(i<3){
            entries.add(new BarEntry((float)p.getPartyScore(), i));
            colors.add(p.getColor());
            labels.add(p.getPartyName());
            /*}else if (i>=(parties.size()-3)){
                entries.add(new BarEntry((float)p.getPartyScore(), f));
                colors.add(p.getColor());
                labels.add(p.getPartyName());
                f++;
            }*/
            i++;

        }

        BarDataSet dataset = new BarDataSet(entries, administrativeArea.getName());

        dataset.setColors(colors);
        BarData data = new BarData(labels, dataset);
        barChart.setBackgroundColor(Color.TRANSPARENT);
        barChart.setGridBackgroundColor(Color.TRANSPARENT);
        barChart.setData(data);
        // barChart.getAxisRight().setDrawAxisLine(false); // Abajo.
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawAxisLine(false); // Arriba.
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        // barChart.getXAxis().setDrawAxisLine(false); // Izquierda.
        barChart.setDescription("% de afinidad.");
        barChart.animateXY(5000, 5000);
        barChart.invalidate();
    }

    public void shareit()
    {
        //View view =  findViewById(R.id.result);//your layout idr
        View view = getWindow().getDecorView();
        view.getRootView();
        String state = Environment.getExternalStorageState();



        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            File picDir  = new File(Environment.getExternalStorageDirectory()+ "/myPic");
            if (!picDir.exists())
            {
                picDir.mkdir();
            }
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap bitmap = view.getDrawingCache();

/**Funciones para detectar el tema y colores/imagenes de fondo**/
            final Canvas canvas = new Canvas(bitmap);

// Get current theme to know which background to use
            final Resources.Theme theme = ResultActivity.this.getTheme();
            final TypedArray ta = theme
                    .obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
            final int res = ta.getResourceId(0, 0);
            final Drawable background= ResultActivity.this.getResources().getDrawable(res);

// Draw background
            background.draw(canvas);

// Draw views
            view.draw(canvas);



            String fileName = "test-picture" + ".jpg";
            picFile = new File(picDir + "/" + fileName);
            try
            {
                picFile.createNewFile();
                FileOutputStream picOut = new FileOutputStream(picFile);
                bitmap.setDensity(view.getResources().getDisplayMetrics().densityDpi);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), (int)(bitmap.getHeight()));

                boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, picOut);

                picOut.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            view.destroyDrawingCache();
        } else {
            //Error

        }

        DialogoSeleccion ds = new DialogoSeleccion();
        ds.show(getFragmentManager(), "Selección");

        //share("whatsapp",picFile.getAbsolutePath().toString(),"your comment");
        /*
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Estos han sido mis resultados en el Test Elecciones 2015. Descarga la app en: https://goo.gl/4NvZPT");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(picFile.getAbsolutePath()));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));*/
    }



    public void seleccionado(String net_share){
        if(net_share.equalsIgnoreCase("Facebook")) share("facebook",picFile.getAbsolutePath().toString(),SHARE_COMMENT);
        else if(net_share.equalsIgnoreCase("Twitter")) share("twitter",picFile.getAbsolutePath().toString(),SHARE_COMMENT);
        else if(net_share.equalsIgnoreCase("Whatsapp")) share("whatsapp",picFile.getAbsolutePath().toString(),SHARE_COMMENT);
    }
    public void share(String nameApp, String imagePath, String message) {
        try {
            List<Intent> targetedShareIntents = new ArrayList<Intent>();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("image/jpeg");
            List<ResolveInfo> resInfo = getPackageManager()
                    .queryIntentActivities(share, 0);
            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    Intent targetedShare = new Intent(
                            android.content.Intent.ACTION_SEND);
                    targetedShare.setType("image/jpeg"); // put here your mime
                    // type
                    if (info.activityInfo.packageName.toLowerCase().contains(
                            nameApp)
                            || info.activityInfo.name.toLowerCase().contains(
                            nameApp)) {
                        targetedShare.putExtra(Intent.EXTRA_SUBJECT,
                                "Sample Photo");
                        targetedShare.putExtra(Intent.EXTRA_TEXT, message);
                        targetedShare.putExtra(Intent.EXTRA_STREAM,
                                Uri.fromFile(new File(imagePath)));
                        targetedShare.setPackage(info.activityInfo.packageName);
                        targetedShareIntents.add(targetedShare);
                    }
                }
                Intent chooserIntent = Intent.createChooser(
                        targetedShareIntents.remove(0), "Select app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        targetedShareIntents.toArray(new Parcelable[] {}));
                startActivity(chooserIntent);
            }
        } catch (Exception e) {
            Log.v("VM",
                    "Exception while sending image on" + nameApp + " "
                            + e.getMessage());
        }
    }
    /**
     * Subclase para enviar datos a la base de datos de la nube en segundo plano.
     */
    private class UploadResult extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog pDialog;
        private static final String SUCCESS = "success";
        private static final String SUCCESS_NUM = "1";
        int i = 0;
        protected Boolean doInBackground(Void... voids) {

            NetFunctions functions = new NetFunctions();
            if(this.isCancelled()) return false;
            JSONObject json = functions.addScore(parties.get(parties.size()-1).getPartyId(),
                    administrativeArea.getAdminAreaId());
            if(json==null) return false;
            if(this.isCancelled()) return false;
            // Tiene que ir entre try/catch.
            try {
                if(this.isCancelled()) return false;
                if(json.getString(SUCCESS).equals(SUCCESS_NUM)){
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Boolean result) {
            pDialog.dismiss();
            if(result){
                Toast.makeText(getApplicationContext(), "Cálculo terminado.", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ResultActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(true);
            pDialog.show();
        }
    }
}
