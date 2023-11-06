package com.example.web.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {
    private String apiHost = "http://localhost:8081";

    private final RestTemplate restTemplate;

    private ObjectMapper mapper;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.mapper = new ObjectMapper();
    }

    public JsonNode getForecast(String endpoint, Map<String, String> params) {
        String url = buildUrl(endpoint, params);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            return convertJsonToNode(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw new RuntimeException("Failed to retrieve data", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to JsonNode", e);
        }
    }

    private String buildUrl(String endpoint, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(apiHost).append(endpoint).append("?");
        params.forEach((key, value) -> urlBuilder.append(key).append("=").append(value).append("&"));
        return urlBuilder.toString();
    }

    private JsonNode convertJsonToNode(String json) throws IOException {
        return mapper.readTree(json);
    }

    public JsonNode getForecastByDateAndPartOfDay(String date, String partOfDay) {
        Map<String, String> params = Map.of("date", date, "partOfDay", partOfDay);
        return getForecast("/api/weather/forecast", params);
    }

    public JsonNode getPeipsiForecastByDateAndPartOfDay(String date, String partOfDay) {
        Map<String, String> params = Map.of("date", date, "partOfDay", partOfDay);
        return getForecast("/api/weather/forecasts/peipsi", params);
    }

    public JsonNode getPlaceForecastByDateAndPartOfDay(String date, String partOfDay, String place) {
        Map<String, String> params = Map.of("date", date, "partOfDay", partOfDay, "place", place);
        return getForecast("/api/weather/forecasts/place", params);
    }

    public List<String> getAvailablePlacesByDate(String date) {
        String url = apiHost + "/api/weather/forecasts/places?date=" + date;
        String[] placesArray = restTemplate.getForObject(url, String[].class);
        return Arrays.asList(placesArray);
    }

    public List<JsonNode> getDayForecasts(int limit) {
        String url = apiHost + "/api/weather/forecasts?partOfDay=day&limit=" + limit;

        JsonNode rootNode = restTemplate.getForObject(url, JsonNode.class);

        List<JsonNode> forecasts = new ArrayList<>();
        if (rootNode.isArray()) {
            rootNode.forEach(forecasts::add);
        }

        return forecasts;
    }
}
