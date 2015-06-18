package clickabus.elecciones.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
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
import clickabus.elecciones.model.DialogoSeleccion;
import clickabus.elecciones.model.Party;
import clickabus.elecciones.model.PartyAdminArea;

public class StatisticsActivity extends ActionBarActivity {

    private PieChart pie_chart;
    private RelativeLayout main_layout;

    private String comunidad = "";

    private ArrayList<Party> parties = new ArrayList<>();
    private ArrayList<PartyAdminArea> partyAdminAreas = new ArrayList<>();

    private int elements_num = 0;

    private File picFile;
    private float[] y_data;
    private String[] x_data;

    private String SHARE_COMMENT;
    private AdministrativeArea administrativeArea;

    private static final String ADMIN_AREA = "admin_area";

    public static int FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        FLAG = 1;
        Bundle bundleObject = getIntent().getExtras();
        administrativeArea = (AdministrativeArea) bundleObject.getSerializable(ADMIN_AREA);

        comunidad = administrativeArea.getName();
        SHARE_COMMENT = "Resultados de " + comunidad + " en @testelecciones. Descarga la app gratis en: https://goo.gl/4NvZPT";
        main_layout = (RelativeLayout)findViewById(R.id.mainLayout);
        Drawable background = main_layout.getBackground();
        background.setAlpha(80);

        pie_chart = new PieChart(this);
        main_layout.addView(pie_chart);

        DownloadParties dp = new DownloadParties();
        if(isNetworkAvailable()) dp.execute();
        else Toast.makeText(getApplicationContext(), "Por favor, active su conexión a internet.", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onStop(){
        super.onStop();
        FLAG = 0;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    private void configureChart(){
        // Background de la activity.
        pie_chart.setBackgroundColor(Color.TRANSPARENT);
        pie_chart.setCenterTextColor(Color.BLACK);

        // Define si va a usar valores porcentajes.
        pie_chart.setUsePercentValues(true);
        pie_chart.setDescription(administrativeArea.getName() +" (" + getTamañoMuestra(getPartiesInArea())+ " muestras)");
        pie_chart.setDescriptionTextSize(18f);

        // Círculos del centro.
        pie_chart.setDrawHoleEnabled(true);
        pie_chart.setHoleColorTransparent(true);
        pie_chart.setHoleRadius(7);
        pie_chart.setTransparentCircleRadius(10);

        // Activa la rotación.
        pie_chart.setRotationAngle(0);
        pie_chart.setRotationEnabled(true);

        pie_chart.animateXY(5000, 5000);
        // Cuando pulsas.
        pie_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if(entry==null) return;
                Toast.makeText(getApplicationContext(), x_data[entry.getXIndex()] + " = " + entry.getVal()*100 + "%", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        addData();

        // Leyenda del gráfico.
        Legend l = pie_chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    private ArrayList<Party> getPartiesInArea(){
        ArrayList<Party> partiesInArea = new ArrayList<>();
        for(Party p : parties){
            for(PartyAdminArea pa : partyAdminAreas){
                if(pa.getAreaId()==administrativeArea.getAdminAreaId() && p.getPartyId()==pa.getPartyId()){
                    p.setPartyScore(pa.getScore());
                    partiesInArea.add(p);
                    break;
                }
            }
        }
        return partiesInArea;
    }
    private int getTamañoMuestra(ArrayList<Party> parties){
        int muestras= 0;
        for(Party p : parties){
            muestras += p.getPartyScore();
         }
    return muestras;
    }

    private void addData(){
        ArrayList<Entry> y_vals = new ArrayList<>();
        for(int i = 0; i< y_data.length; i++){
            y_vals.add(new Entry(y_data[i], i));
        }
        ArrayList<String> x_vals = new ArrayList<>();

        for(int i = 0; i< x_data.length; i++){
            x_vals.add(x_data[i]);
        }

        // Esto es el espacio entre porciones y lo que se agranda cuando pulsas.
        PieDataSet data_set = new PieDataSet(y_vals,"");
        data_set.setSliceSpace(3);
        data_set.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<>();
        for(Party p: parties){
            if(p.getPartyScore()!=0) colors.add(p.getColor());
        }


        // Esto con los atributos de los nombres de partidos y porcentajes que aparecen sobre
        // el gráfico.
        data_set.setColors(colors);
        PieData data = new PieData(x_vals, data_set);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        pie_chart.setData(data);
        pie_chart.highlightValues(null);
        pie_chart.invalidate();
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

    private float[] getPercentages(){
        float total = 0;

        for(Party p : parties){
            total+=p.getPartyScore();
            if(p.getPartyScore()!=0) elements_num++;
        }
        float[] y_data = new float[elements_num];

        int i = 0;
        for(Party p : parties){
            if(p.getPartyScore()!=0){
                y_data[i] = p.getPartyScore()/total;
                i++;
            }
        }
        return y_data;
    }

    private String[] getNames(){
        String[] x_data = new String[elements_num];

        int i = 0;
        for(Party p : parties){
            if(p.getPartyScore()!=0){
                x_data[i] = p.getPartyName();
                i++;
            }

        }
        return x_data;
    }

    public void shareit()
    {
       // View view =  findViewById(R.id.mainLayout);//your layout idr
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
            final Resources.Theme theme = StatisticsActivity.this.getTheme();
            final TypedArray ta = theme
                    .obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
            final int res = ta.getResourceId(0, 0);
            final Drawable background= StatisticsActivity.this.getResources().getDrawable(res);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Subclase para descargar la base de datos de la nube en segundo plano.
     */
    private class DownloadParties extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog pDialog;
        private static final String SUCCESS = "success";
        private static final String SUCCESS_NUM = "1";

        private static final String PARTIES_JSON = "parties";
        private static final String PARTY_ID = "id_party";
        private static final String PARTY_NAME = "name";
        private static final String PARTY_SCORE = "score";

        private static final String PARTY_ADMIN_AREAS_JSON = "party_areas";
        private static final String PARTY_ADMIN_ID = "party_id";
        private static final String PARTY_ADMIN_AREA_ID = "area_id";
        private static final String PARTY_ADMIN_AREA_SCORE = "score";

        protected Boolean doInBackground(Void... voids) {

            NetFunctions functions = new NetFunctions();
            JSONObject json = functions.getAll();

            // Tiene que ir entre try/catch.
            try {
                if(json.getString(SUCCESS).equals(SUCCESS_NUM)){

                    // Obtengo partidos.
                    JSONArray jsonParties = json.getJSONArray(PARTIES_JSON);

                    for (int i = 0; i < jsonParties.length(); i++) {
                        JSONObject c = jsonParties.getJSONObject(i);
                        Party party = new Party(c.getInt(PARTY_ID), c.getString(PARTY_NAME), 0);
                        parties.add(party);
                    }

                    // Obtengo relaciones comunidad-partido.
                    JSONArray jsonPartyAdminAreas = json.getJSONArray(PARTY_ADMIN_AREAS_JSON);

                    for (int i = 0; i < jsonPartyAdminAreas.length(); i++) {
                        JSONObject c = jsonPartyAdminAreas.getJSONObject(i);
                        PartyAdminArea partyAdminArea = new PartyAdminArea(c.getInt(PARTY_ADMIN_ID),
                                c.getInt(PARTY_ADMIN_AREA_ID), c.getInt(PARTY_ADMIN_AREA_SCORE));
                        partyAdminAreas.add(partyAdminArea);
                    }

                    parties = getPartiesInArea();

                    Collections.sort(parties, new Comparator<Party>() {
                        @Override
                        public int compare(Party p1, Party p2) {
                            return new Integer(p2.getPartyScore()).compareTo(new Integer(p1.getPartyScore()));
                        }
                    });
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
                Toast.makeText(getApplicationContext(), "Se han cargado los datos correctamente.", Toast.LENGTH_LONG).show();
                y_data = getPercentages();
                x_data = getNames();
                configureChart();
            }
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StatisticsActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

}
