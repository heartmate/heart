package com.HeartmatePack.heartmate.bean;



public class Heart_Rate {
    private String rate;
    private String rate_date;
    private String rate_time;

    public Heart_Rate() {
    }

    public Heart_Rate(String rate, String rate_date, String rate_time) {
        this.rate = rate;
        this.rate_date = rate_date;
        this.rate_time = rate_time;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate_date() {
        return rate_date;
    }

    public void setRate_date(String rate_date) {
        this.rate_date = rate_date;
    }

    public String getRate_time() {
        return rate_time;
    }

    public void setRate_time(String rate_time) {
        this.rate_time = rate_time;
    }
}
