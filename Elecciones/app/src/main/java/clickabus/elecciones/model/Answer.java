package clickabus.elecciones.model;

public class Answer {

    private int answer_id;
    private int question_id;
    private String answer;

    public Answer(int answer_id, int question_id, String answer){
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.answer = answer;
    }

    public int getAnswerId() {
        return answer_id;
    }

    public void setAnswerId(int answer_id) {
        this.answer_id = answer_id;
    }

    public int getQuestionId() {
        return question_id;
    }

    public void setQuestionId(int question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
