package clickabus.elecciones.model;

import java.io.Serializable;

public class AdministrativeArea implements Serializable{

    private int admin_area_id;
    private String name, short_name;

    public AdministrativeArea(int admin_area_id, String name, String short_name) {
        this.admin_area_id = admin_area_id;
        this.name = name;
        this.short_name = short_name;
    }

    public int getAdminAreaId() {
        return admin_area_id;
    }

    public void setAdminAreaId(int admin_area_id) {
        this.admin_area_id = admin_area_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return short_name;
    }

    public void setShortName(String short_name) {
        this.short_name = short_name;
    }
}
