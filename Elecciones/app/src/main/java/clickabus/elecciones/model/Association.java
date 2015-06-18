package clickabus.elecciones.model;

public class Association {

    private int answer_id;
    private int party_id;
    private int score;

    public Association(int answer_id, int party_id, int score) {
        this.answer_id = answer_id;
        this.party_id = party_id;
        this.score = score;
    }

    public int getAnswerId() {
        return answer_id;
    }

    public void setAnswerId(int answer_id) {
        this.answer_id = answer_id;
    }

    public int getPartyId() {
        return party_id;
    }

    public void setPartyId(int party_id) {
        this.party_id = party_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
