package com.example.background.controllers;

import com.example.background.entities.PartOfDayForecast;
import com.example.background.entities.PeipsiForecast;
import com.example.background.entities.PlaceForecast;
import com.example.background.services.RetrieveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
public class WeatherController {
    @Autowired
    private RetrieveService retrieveService;

    @GetMapping("/api/weather/forecast")
    public ResponseEntity<PartOfDayForecast> getForecastByDateAndPartOfDay(
            @RequestParam String date, @RequestParam String partOfDay) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Optional<PartOfDayForecast> optionalForecast = retrieveService.getPartOfDayForecast(localDate, partOfDay);
        if (optionalForecast.isPresent()) {
            return ResponseEntity.ok(optionalForecast.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/weather/forecasts/peipsi")
    public ResponseEntity<PeipsiForecast> getPeipsiForecastByDateAndPartOfDay(
            @RequestParam String date, @RequestParam String partOfDay) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Optional<PeipsiForecast> optionalForecast = retrieveService.getPeipsiForecast(localDate, partOfDay);
        if (optionalForecast.isPresent()) {
            return ResponseEntity.ok(optionalForecast.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/weather/forecasts/place")
    public ResponseEntity<PlaceForecast> getPlaceForecastByDateAndPartOfDay(
            @RequestParam String date, @RequestParam String partOfDay, @RequestParam String place) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Optional<PlaceForecast> optionalForecast = retrieveService.getPlaceForecast(localDate, partOfDay, place);
        if (optionalForecast.isPresent()) {
            return ResponseEntity.ok(optionalForecast.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/weather/forecasts/places")
    public List<String> getAvailablePlacesByDate(@RequestParam String date)
    {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return retrieveService.getAvailablePlacesByDate(localDate);
    }

    @GetMapping("/api/weather/forecasts")
    public List<PartOfDayForecast> getForecastsByPartOfDay(
            @RequestParam String partOfDay, @RequestParam (required = false) Optional<Integer> limit) {
        List<PartOfDayForecast> forecasts;
        int defaultLimit = 7;
        if (limit.isPresent()) {
            forecasts = retrieveService.getPartOfDayForecasts(partOfDay, limit.get());
        } else {
            forecasts = retrieveService.getPartOfDayForecasts(partOfDay, defaultLimit);
        }
        return forecasts;
    }
}
