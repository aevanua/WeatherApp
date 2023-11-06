package com.example.background.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "peipsi_forecasts")
public class PeipsiForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String partOfDay;
    public String getPartOfDay() {return partOfDay;}
    public void setPartOfDay(String partOfDay) {this.partOfDay = partOfDay;}
    private LocalDate date;
    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    private String text;
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    public PeipsiForecast(String partOfDay, LocalDate date, String text) {
        this.partOfDay = partOfDay;
        this.date = date;
        this.text = text;
    }
    public PeipsiForecast()
    {
        this.partOfDay = null;
        this.date = null;
        this.text = null;
    }
}