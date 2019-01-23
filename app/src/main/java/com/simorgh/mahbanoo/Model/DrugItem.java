package com.simorgh.mahbanoo.Model;

import java.util.Calendar;

public class DrugItem {
    private String drugName;
    private Calendar id = Calendar.getInstance();

    public DrugItem() {
    }

    public Calendar getId() {
        return id;
    }

    public DrugItem(String drugName, Calendar id) {
        this.drugName = drugName;
        this.id.setTimeInMillis(id.getTimeInMillis());
    }

    public void setId(Calendar id) {
        this.id.setTimeInMillis(id.getTimeInMillis());
    }

    public DrugItem(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
}
