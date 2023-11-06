package com.example.web.controllers;

import com.example.web.Forecast;
import com.example.web.services.ApiService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewController {
    @Autowired
    private ApiService apiService;

    @GetMapping("/")
    public String index(@RequestParam(value = "date", required = false) String date,
                        @RequestParam(value = "place", required = false) String place,
                        Model model)
    {
        if (date == null || date.isEmpty()) {
            date = LocalDate.now().toString();
        }

        List<JsonNode> jsonDayForecasts = apiService.getDayForecasts(7);
        List<Forecast> dayForecasts = new ArrayList<>();
        for (JsonNode jsonDayForecast : jsonDayForecasts) {
            dayForecasts.add(convertJsonNodeToForecast(jsonDayForecast));
        }
        model.addAttribute("dayForecasts", dayForecasts);

        boolean dateIsValid = dateIsValid(date, dayForecasts);

        model.addAttribute("dateIsValid", dateIsValid);

        if (dateIsValid) {
            String selectedDate = date;
            model.addAttribute("selectedDate", selectedDate);

            model.addAttribute("dayForecast", getForecast(selectedDate, "day"));
            model.addAttribute("nightForecast", getForecast(selectedDate, "night"));

            List<String> places = apiService.getAvailablePlacesByDate(selectedDate);
            model.addAttribute("places", places);

            if (place != null && !place.isEmpty()) {
                model.addAttribute("selectedPlace", place);

                if (getPlaceForecast(selectedDate, "night", place ) != null ) {
                    model.addAttribute("placeDayForecast", getPlaceForecast(selectedDate, "day", place));
                    model.addAttribute("placeNightForecast", getPlaceForecast(selectedDate, "night", place));
                }
            }

            if (getPeipsiForecast(selectedDate, "night") != null)
            {
                model.addAttribute("peipsiDayForecast", getPeipsiForecast(selectedDate, "day"));
                model.addAttribute("peipsiNightForecast", getPeipsiForecast(selectedDate, "night"));
            }
        }

        return "index";
    }

    private boolean dateIsValid(String date, List<Forecast> forecasts) {
        for (int i = 0; i < forecasts.size(); i++) {
            if (forecasts.get(i).getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    private Forecast convertJsonNodeToForecast(JsonNode jsonNode) {
        String date = jsonNode.has("date") ? jsonNode.get("date").asText() : null;
        String phenomenon = jsonNode.has("phenomenon") ? jsonNode.get("phenomenon").asText() : null;
        String text = jsonNode.has("text") ? jsonNode.get("text").asText() : null;
        Integer tempMin = jsonNode.has("tempMin") ? jsonNode.get("tempMin").asInt() : null;
        Integer tempMax = jsonNode.has("tempMax") ? jsonNode.get("tempMax").asInt() : null;

        return new Forecast(date, phenomenon, text, tempMin, tempMax);
    }

    private Forecast getForecast(String date, String partOfDay) {
        JsonNode jsonForecast = apiService.getForecastByDateAndPartOfDay(date, partOfDay);
        return createForecastFromJson(jsonForecast);
    }

    private Forecast getPeipsiForecast(String date, String partOfDay)  {
        JsonNode jsonForecast = apiService.getPeipsiForecastByDateAndPartOfDay(date, partOfDay);
        return createForecastFromJson(jsonForecast);
    }

    private Forecast getPlaceForecast(String date, String partOfDay, String place) {
        JsonNode jsonPlaceForecast = apiService.getPlaceForecastByDateAndPartOfDay(date, partOfDay, place);
        return createForecastFromJson(jsonPlaceForecast);
    }

    private Forecast createForecastFromJson(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isEmpty()) {
            return null;
        }
        String phenomenon = jsonNode.path("phenomenon").asText(null);
        String text = jsonNode.path("text").asText(null);
        Integer tempMin = jsonNode.path("tempMin").isInt() ? jsonNode.path("tempMin").asInt() : null;
        Integer tempMax = jsonNode.path("tempMax").isInt() ? jsonNode.path("tempMax").asInt() : null;
        return new Forecast(phenomenon, text, tempMin, tempMax);
    }
}
