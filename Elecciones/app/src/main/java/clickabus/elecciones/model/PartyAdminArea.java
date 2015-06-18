package clickabus.elecciones.model;

public class PartyAdminArea {

    private int party_id;
    private int area_id;
    private int score;

    public PartyAdminArea(int party_id, int area_id, int score) {
        this.party_id = party_id;
        this.area_id = area_id;
        this.score = score;
    }

    public int getPartyId() {
        return party_id;
    }

    public void setPartyId(int party_id) {
        this.party_id = party_id;
    }

    public int getAreaId() {
        return area_id;
    }

    public void setAreaId(int area_id) {
        this.area_id = area_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
