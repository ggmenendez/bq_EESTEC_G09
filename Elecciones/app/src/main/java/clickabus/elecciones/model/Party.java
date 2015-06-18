package clickabus.elecciones.model;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.clickabus.elecciones.R;

import java.util.ArrayList;

public class Party {

    private int party_id;
    private String party_name;
    private int party_score;
    private int answered_questions;
    private Integer color;
    private int icon;

    public int getIcon() {
        return icon;
    }

    public Party(int party_id, String party_name, int party_score) {
        this.party_id = party_id;
        this.party_name = party_name;
        this.party_score = party_score;
        this.answered_questions = 0;

        switch (party_id){
            case 1:
                color = Color.parseColor("#009bd4");
                icon = R.drawable.pp;
                break;
            case 2:
                color = Color.parseColor("#ed1c23");
                icon = R.drawable.psoe;
                break;
            case 3:
                color = Color.parseColor("#69215d");
                icon = R.drawable.podemos;
                break;
            case 4:
                color = Color.parseColor("#f58733");
                icon = R.drawable.ciudadanos;
                break;
            case 5:
                color = Color.parseColor("#e30079");
                icon = R.drawable.upyd;
                break;
            case 6:
                color = Color.parseColor("#117850");
                icon = R.drawable.iu;
                break;
            case 7:
                color = Color.parseColor("#b0bd20");
                icon = R.drawable.pacma;
                break;
            case 8:
                color = Color.parseColor("#f5cc18");
                icon = R.drawable.par;
                break;
            case 9:
                color = Color.parseColor("#ab0215");
                icon = R.drawable.cha;
                break;
            case 10:
                color = Color.parseColor("#bfc200");
                icon = R.drawable.prc;
                break;
            case 11:
                color = Color.parseColor("#71b028");
                icon = R.drawable.vox;
                break;
            case 12:
                color = Color.parseColor("#001d57");
                icon = R.drawable.ciudadanos;
                break;
            case 13:
                color = Color.parseColor("#ffb319");
                icon = R.drawable.er;
                break;
            case 14:
                color = Color.parseColor("#e65825");
                icon = R.drawable.compromis;
                break;
            case 15:
                color = Color.parseColor("#246b34");
                icon = R.drawable.pnv;
                break;
            case 16:
                color = Color.parseColor("#b3c702");
                icon = R.drawable.bildu;
                break;
            case 17:
                color = Color.parseColor("#d12f13");
                icon = R.drawable.geroabai;
                break;
            case 18:
                color = Color.parseColor("#023666");
                icon = R.drawable.upn;
                break;
            case 19:
                color = Color.parseColor("#003557");
                icon = R.drawable.foro;
                break;
            case 20:
                color = Color.parseColor("#ffea00");
                icon = R.drawable.can;
                break;
            case 21:
                color = Color.parseColor("#6db33e");
                icon = R.drawable.pari;
                break;
            case 22:
                color = Color.parseColor("#8ed8fa");
                icon = R.drawable.bng;
                break;
        }
    }

    public int getPartyId() {
        return party_id;
    }

    public void setPartyId(int party_id) {
        this.party_id = party_id;
    }

    public String getPartyName() {
        return party_name;
    }

    public void setPartyName(String party_name) {
        this.party_name = party_name;
    }

    public int getPartyScore() {
        return party_score;
    }

    public void setPartyScore(int party_score) {
        this.party_score = party_score;
    }

    public void newAnsweredQuestion() {
        this.answered_questions++;
    }

    public int getAnsweredQuestion() {
        return answered_questions;
    }

    public Integer getColor(){
        return color;
    }
}
