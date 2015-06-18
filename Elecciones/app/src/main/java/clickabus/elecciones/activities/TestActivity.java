package clickabus.elecciones.activities;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clickabus.elecciones.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import clickabus.elecciones.database.DataBaseHelper;
import clickabus.elecciones.model.Answer;
import clickabus.elecciones.model.Question;
import clickabus.elecciones.model.UserAnswer;

import java.util.ArrayList;

public class TestActivity extends ActionBarActivity {

    private int pregunta_actual = 1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private static final int NUM_OF_QUESTIONS = 21;
    private static final String USER_ANSWERS_KEY = "user_answers";

    private TextView txtQuestion, txtAnswer1, txtAnswer2, txtAnswer3, txtAnswer4;

    private DataBaseHelper database;

    private int retroceso = 0;

    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
    private ArrayList<UserAnswer> user_answers = new ArrayList<>();

    private Question actual_question;
    private  ArrayList<Answer> actual_answers = new ArrayList<>();

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        bundle = getIntent().getExtras();

        txtQuestion = (TextView)findViewById(R.id.txtQuestion);
        txtAnswer1 = (TextView)findViewById(R.id.txtAnswer1);
        txtAnswer2 = (TextView)findViewById(R.id.txtAnswer2);
        txtAnswer3 = (TextView)findViewById(R.id.txtAnswer3);
        txtAnswer4 = (TextView)findViewById(R.id.txtAnswer4);



        database = new DataBaseHelper(getApplicationContext());

        questions = database.getQuestions();
        answers = database.getAnswers();

        getActual();
        updateViews();

        mProgress = (ProgressBar) findViewById(R.id.pbTestProgress);
        mProgress.setMax(NUM_OF_QUESTIONS);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_back){
            retroceder();
        }

        if(id == R.id.action_next){
            avanzar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getActual(){

        actual_question = null;
        actual_answers.clear();

        // Obtengo la pregunta actual.
        for(Question ques : questions){
            if(ques.getQuestionId()==pregunta_actual){
                actual_question = ques;
                break;
            }
        }
        // Obtengo las respuestas actuales.
        for(Answer ans : answers){
            if(actual_question.getQuestionId()==ans.getQuestionId()){
                actual_answers.add(ans);
            }
        }
    }

    private void avanzar(){
        // Si se ha acabado el test, muestro el resultado. Si no, cargo la siguiente pregunta.
        retroceso = 0;
        if(pregunta_actual>NUM_OF_QUESTIONS){
            Intent i = new Intent(getApplicationContext(), ResultActivity.class);
            bundle.putSerializable(USER_ANSWERS_KEY, user_answers);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }else {
            mProgressStatus++;
            mProgress.setProgress(mProgressStatus);
            getActual();
            updateViews();
        }
    }
    private void updateViews(){

        // Borro textos.
        txtQuestion.setText("");
        txtAnswer1.setText("");
        txtAnswer2.setText("");
        txtAnswer3.setText("");
        txtAnswer4.setText("");

        // Actualizo textos con preguntas y respuestas actuales.
        txtQuestion.setText(actual_question.getQuestion());
        txtAnswer1.setText(actual_answers.get(0).getAnswer());
        txtAnswer2.setText(actual_answers.get(1).getAnswer());
        if(actual_answers.size()>2){
            txtAnswer3.setVisibility(View.VISIBLE);
            txtAnswer3.setText(actual_answers.get(2).getAnswer());
        }else{
            txtAnswer3.setVisibility(View.INVISIBLE);
        }
        if(actual_answers.size()>3){
            txtAnswer4.setVisibility(View.VISIBLE);
            txtAnswer4.setText(actual_answers.get(3).getAnswer());
        }else{
            txtAnswer4.setVisibility(View.INVISIBLE);
        }
        pregunta_actual++;
    }

    public void retroceder(){
        if(pregunta_actual<=2 || retroceso>=1){
            return;
        }
        mProgressStatus--;
        mProgress.setProgress(mProgressStatus);
        if(user_answers.size()>0 && user_answers.size()==(pregunta_actual-1)) user_answers.remove(user_answers.size()-1);
        pregunta_actual= pregunta_actual-2;
        getActual();
        updateViews();
        // displayInterstitial();
        retroceso++;
    }
    /**
     * Método que se ejecuta al responder una pregunta.
     * @param w: respuesta pulsada.
     */
    public void onClick(View w){

        UserAnswer new_answer = null;
        retroceso=0;
        // Según el id de la view pulsada, creo una nueva respuesta del usuario.
        switch (w.getId()){
            case R.id.txtAnswer1:
                new_answer = new UserAnswer(actual_question.getQuestionId(), actual_answers.get(0).getAnswerId());
                break;
            case R.id.txtAnswer2:
                new_answer = new UserAnswer(actual_question.getQuestionId(), actual_answers.get(1).getAnswerId());
                break;
            case R.id.txtAnswer3:
                new_answer = new UserAnswer(actual_question.getQuestionId(), actual_answers.get(2).getAnswerId());
                break;
            case R.id.txtAnswer4:
                new_answer = new UserAnswer(actual_question.getQuestionId(), actual_answers.get(3).getAnswerId());
                break;
            default:
                break;
        }
        // Añado la nueva respuesta a la lista de respuestas
        user_answers.add(new_answer);

        mProgressStatus++;
        mProgress.setProgress(mProgressStatus);

        // Si se ha acabado el test, muestro el resultado. Si no, cargo la siguiente pregunta.
        if(pregunta_actual>NUM_OF_QUESTIONS){
            Intent i = new Intent(getApplicationContext(), ResultActivity.class);
            bundle.putSerializable(USER_ANSWERS_KEY, user_answers);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }else {
            getActual();
            updateViews();
        }

    }
}
