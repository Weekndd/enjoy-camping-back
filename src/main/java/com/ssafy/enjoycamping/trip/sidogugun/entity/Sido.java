package com.ssafy.enjoycamping.trip.sidogugun.entity;

public class Sido {
    private int sidoCode;
    private String sidoName;

    // Constructors
    public Sido() {}

    public Sido(int sidoCode, String sidoName) {
        this.sidoCode = sidoCode;
        this.sidoName = sidoName;
    }

    // Getters and Setters
    public int getSidoCode() {
        return sidoCode;
    }

    public void setSidoCode(int sidoCode) {
        this.sidoCode = sidoCode;
    }

    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }
}