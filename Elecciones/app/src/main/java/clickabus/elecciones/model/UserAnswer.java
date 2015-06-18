package clickabus.elecciones.model;

import java.io.Serializable;

public class UserAnswer implements Serializable{

    private int id_question;
    private int id_answer;

    public UserAnswer(int id_question, int id_answer) {
        this.id_question = id_question;
        this.id_answer = id_answer;
    }

    public int getAnswerId() {
        return id_answer;
    }

    public void setAnswerId(int id_answer) {
        this.id_answer = id_answer;
    }

    public int getQuestionId() {
        return id_question;
    }

    public void setQuestionId(int id_question) {
        this.id_question = id_question;
    }
}
