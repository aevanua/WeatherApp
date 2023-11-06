package com.example.background.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "part_of_day_forecasts")
public class PartOfDayForecast   {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String partOfDay;
    public String getPartOfDay() {return partOfDay;}
    public void setPartOfDay(String partOfDay) {this.partOfDay = partOfDay;}
    private LocalDate date;
    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    private String phenomenon;
    public String getPhenomenon() {return phenomenon;}
    public void setPhenomenon(String phenomenon) {this.phenomenon = phenomenon;}

    @Column(columnDefinition = "TEXT")
    private String text;
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    private Integer tempMin;
    public Integer getTempMin() {return tempMin;}
    public void setTempMin(Integer tempMin) {this.tempMin = tempMin;}

    private Integer tempMax;
    public Integer getTempMax() {return tempMax;}
    public void setTempMax(Integer tempMax) {this.tempMax = tempMax;}


    public PartOfDayForecast(String partOfDay, LocalDate date, String phenomenon, String text, Integer tempMin, Integer tempMax) {
        this.partOfDay = partOfDay;
        this.date = date;
        this.phenomenon = phenomenon;
        this.text = text;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public PartOfDayForecast()
    {
        this.partOfDay = null;
        this.date = null;
        this.phenomenon = null;
        this.text = null;
        this.tempMin = null;
        this.tempMax = null;
    }
}