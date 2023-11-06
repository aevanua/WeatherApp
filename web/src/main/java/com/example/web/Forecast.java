package com.example.web;

public class Forecast {

    private String date;
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    private String phenomenon;

    public String getPhenomenon() {return phenomenon;}
    public void setPhenomenon(String phenomenon) {this.phenomenon = phenomenon;}

    private String text;
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    private Integer tempMin;
    public Integer getTempMin() {return tempMin;}
    public void setTempMin(Integer tempMin) {this.tempMin = tempMin;}

    private Integer tempMax;
    public Integer getTempMax() {return tempMax;}
    public void setTempMax(Integer tempMax) {this.tempMax = tempMax;}

    public Forecast(String date,String phenomenon, String text, Integer tempMin, Integer tempMax) {
        this.date = date;
        this.phenomenon = phenomenon;
        this.text = text;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public Forecast(String phenomenon, String text, Integer tempMin, Integer tempMax) {
        this.phenomenon = phenomenon;
        this.text = text;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public Forecast (String phenomenon, Integer tempMin, Integer tempMax)
    {
        this.phenomenon = phenomenon;
        this.text = null;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public Forecast (String text)
    {
        this.phenomenon = null;
        this.text = text;
        this.tempMin = null;
        this.tempMax = null;
    }
}
