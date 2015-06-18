package clickabus.elecciones.model;

public class Question {

    private int question_id;
    private String question;

    public Question(int question_id, String question) {
        this.question_id = question_id;
        this.question = question;
    }

    public int getQuestionId() {
        return question_id;
    }

    public void setQuestionId(int question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
