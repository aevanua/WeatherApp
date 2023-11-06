package com.example.background.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "place_forecasts")
public class PlaceForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String partOfDay;
    public String getPartOfDay() {return partOfDay;}
    public void setPartOfDay(String partOfDay) {this.partOfDay = partOfDay;}
    private LocalDate date;
    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    private String name;
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    private String phenomenon;
    public String getPhenomenon() {return phenomenon;}
    public void setPhenomenon(String phenomenon) {this.phenomenon = phenomenon;}

    private Integer tempMin;
    public Integer getTempMin() {return tempMin;}
    public void setTempMin(Integer tempMin) {this.tempMin = tempMin;}

    private Integer tempMax;
    public Integer getTempMax() {return tempMax;}
    public void setTempMax(Integer tempMax) {this.tempMax = tempMax;}



    public PlaceForecast(String partOfDay, LocalDate date, String name, String phenomenon, Integer tempMin, Integer tempMax) {
        this.partOfDay = partOfDay;
        this.date = date;
        this.name = name;
        this.phenomenon = phenomenon;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public PlaceForecast()
    {
        this.partOfDay = null;
        this.date = null;
        this.name = null;
        this.phenomenon = null;
        this.tempMin = null;
        this.tempMax = null;
    }
}