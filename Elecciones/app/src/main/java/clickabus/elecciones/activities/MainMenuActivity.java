package clickabus.elecciones.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.clickabus.elecciones.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import clickabus.elecciones.connection.NetFunctions;
import clickabus.elecciones.database.DataBaseHelper;
import clickabus.elecciones.model.AdministrativeArea;
import clickabus.elecciones.model.Answer;
import clickabus.elecciones.model.Association;
import clickabus.elecciones.model.Party;
import clickabus.elecciones.model.PartyAdminArea;
import clickabus.elecciones.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainMenuActivity extends Activity {

    private static final int TEST_ACTIVITY = 1;
    private static final int STATISTICS_ACTIVITY = 2;

    private static final String SOURCE = "source";

    private static final String SUCCESS = "success";
    private static final String SUCCESS_NUM = "1";

    private static final String VERSION = "version";

    private static final String PARTIES_JSON = "parties";
    private static final String PARTY_ID = "id_party";
    private static final String PARTY_NAME = "name";
    private static final String PARTY_SCORE = "score";

    private static final String QUESTIONS_JSON = "questions";
    private static final String QUESTION_ID = "id_question";
    private static final String QUESTION = "question";

    private static final String ANSWERS_JSON = "answers";
    private static final String ANSWER_ID = "id_answer";
    private static final String ANSWER = "answer";

    private static final String ADMIN_AREAS_JSON = "admin_areas";
    private static final String ADMIN_AREA_ID = "admin_area_id";
    private static final String ADMIN_AREA_NAME = "name";
    private static final String ADMIN_AREA_SHORT_NAME = "short_name";

    private static final String PARTY_ADMIN_AREAS_JSON = "party_areas";
    private static final String PARTY_ADMIN_ID = "party_id";
    private static final String PARTY_ADMIN_AREA_ID = "area_id";
    private static final String PARTY_ADMIN_AREA_SCORE = "score";

    private static final String ASSOCIATIONS_JSON = "associations";

    private ArrayList<Party> parties = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<Answer> answers = new ArrayList<>();
    private ArrayList<Association> associations = new ArrayList<>();
    private ArrayList<AdministrativeArea> admin_areas = new ArrayList<>();
    private ArrayList<PartyAdminArea> partyAdminAreas = new ArrayList<>();


    private Button btnStart;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnStart = (Button)findViewById(R.id.btnStart);
        if(isNetworkAvailable()){
            Button btnStart = (Button)findViewById(R.id.btnStart);
            // Ejecuto el Thread.
            new DownloadData().execute();
        }else{
            Toast.makeText(getApplicationContext(), "Por favor, active su conexión a internet.", Toast.LENGTH_LONG).show();
            finish();
        }



    }


    /**
     * Método que lanza la actividad correspondiente según el botón pulsado en la actividad
     * principal.
     * @param w: Botón pulsado.
     */
    public void btnClick(View w){

        Intent i = null;
        Bundle bundle = null;

        switch(w.getId()){
            case R.id.btnStart:
                i = new Intent(getApplicationContext(), ChooseAdminAreaActivity.class);
                bundle = new Bundle();
                bundle.putInt(SOURCE, TEST_ACTIVITY);
                i.putExtras(bundle);
                break;
            case R.id.btnStatistics:
                i = new Intent(getApplicationContext(), ChooseAdminAreaActivity.class);
                bundle = new Bundle();
                bundle.putInt(SOURCE, STATISTICS_ACTIVITY);
                i.putExtras(bundle);
                break;
            case R.id.btnPrograms:
                i = new Intent(getApplicationContext(), ProgramsActivity.class);
                break;
        }

        if(i!=null){
            startActivity(i);
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
    private class DownloadData extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog pDialog;

        protected Boolean doInBackground(Void... voids) {

            NetFunctions functions = new NetFunctions();
            DataBaseHelper database = new DataBaseHelper(getApplicationContext());
            JSONObject json = functions.getAll();
            JSONObject version = functions.getDatabaseVersion();
            int local_version = database.getDatabaseVersion();

            // Tiene que ir entre try/catch.
            try {
                if(json!=null){
                if(json.getString(SUCCESS).equals(SUCCESS_NUM) && version.getString(SUCCESS).equals(SUCCESS_NUM)) {

                    // Obtengo versión.
                    if (local_version == version.getInt(VERSION)) return true;

                    database.resetTable();

                    // Obtengo partidos.
                    JSONArray jsonParties = json.getJSONArray(PARTIES_JSON);

                    for (int i = 0; i < jsonParties.length(); i++) {
                        JSONObject c = jsonParties.getJSONObject(i);
                        Party party = new Party(c.getInt(PARTY_ID), c.getString(PARTY_NAME), 0);
                        parties.add(party);
                    }

                    // Obtengo preguntas.
                    JSONArray jsonQuestions = json.getJSONArray(QUESTIONS_JSON);

                    for (int i = 0; i < jsonQuestions.length(); i++) {
                        JSONObject c = jsonQuestions.getJSONObject(i);
                        Question question = new Question(c.getInt(QUESTION_ID), c.getString(QUESTION));
                        questions.add(question);
                    }

                    // Obtengo respuestas.
                    JSONArray jsonAnswers = json.getJSONArray(ANSWERS_JSON);

                    for (int i = 0; i < jsonAnswers.length(); i++) {
                        JSONObject c = jsonAnswers.getJSONObject(i);
                        Answer answer = new Answer(c.getInt(ANSWER_ID), c.getInt(QUESTION_ID), c.getString(ANSWER));
                        answers.add(answer);
                    }

                    // Obtengo asociaciones.
                    JSONArray jsonAssociations = json.getJSONArray(ASSOCIATIONS_JSON);

                    for (int i = 0; i < jsonAssociations.length(); i++) {
                        JSONObject c = jsonAssociations.getJSONObject(i);
                        Association association = new Association(c.getInt(ANSWER_ID), c.getInt(PARTY_ID), c.getInt(PARTY_SCORE));
                        associations.add(association);
                    }

                    // Obtengo comunidades.
                    JSONArray jsonAdminAreas = json.getJSONArray(ADMIN_AREAS_JSON);

                    for (int i = 0; i < jsonAdminAreas.length(); i++) {
                        JSONObject c = jsonAdminAreas.getJSONObject(i);
                        AdministrativeArea admin_area = new AdministrativeArea(c.getInt(ADMIN_AREA_ID), c.getString(ADMIN_AREA_NAME),
                                c.getString(ADMIN_AREA_SHORT_NAME));
                        admin_areas.add(admin_area);
                    }

                    // Obtengo relaciones comunidad-partido.
                    JSONArray jsonPartyAdminAreas = json.getJSONArray(PARTY_ADMIN_AREAS_JSON);

                    for (int i = 0; i < jsonPartyAdminAreas.length(); i++) {
                        JSONObject c = jsonPartyAdminAreas.getJSONObject(i);
                        PartyAdminArea partyAdminArea = new PartyAdminArea(c.getInt(PARTY_ADMIN_ID),
                                c.getInt(PARTY_ADMIN_AREA_ID), c.getInt(PARTY_ADMIN_AREA_SCORE));
                        partyAdminAreas.add(partyAdminArea);
                    }

                    database.addVersion(version.getInt(VERSION));
                    database.addAnswers(answers);
                    database.addParties(parties);
                    database.addAssociation(associations);
                    database.addQuestions(questions);
                    database.addAdminArea(admin_areas);
                    database.addPartyArea(partyAdminAreas);

                    database.close();
                    return true;
                }
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
            }else{
                Toast.makeText(getApplicationContext(), "Error al cargar los datos. Por favor, vuelva a intentarlo más tarde.", Toast.LENGTH_LONG).show();
                //finish();
            }
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainMenuActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }
}
