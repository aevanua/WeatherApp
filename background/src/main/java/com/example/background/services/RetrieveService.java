package com.example.background.services;

import com.example.background.entities.PartOfDayForecast;
import com.example.background.entities.PeipsiForecast;
import com.example.background.entities.PlaceForecast;
import com.example.background.repositories.PartOfDayForecastRepository;
import com.example.background.repositories.PeipsiForecastRepository;
import com.example.background.repositories.PlaceForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RetrieveService {

    private final RestTemplate restTemplate;
    private final PartOfDayForecastRepository partOfDayForecastRepository;
    private final PlaceForecastRepository placeForecastRepository;
    private final PeipsiForecastRepository peipsiForecastRepository;

    @Autowired
    public RetrieveService(RestTemplate restTemplate,
                           PartOfDayForecastRepository partOfDayForecastRepository,
                           PlaceForecastRepository placeForecastRepository,
                           PeipsiForecastRepository peipsiForecastRepository)
    {
        this.restTemplate = restTemplate;
        this.partOfDayForecastRepository = partOfDayForecastRepository;
        this.placeForecastRepository = placeForecastRepository;
        this.peipsiForecastRepository = peipsiForecastRepository;
    }

    public Optional<PartOfDayForecast> getPartOfDayForecast(LocalDate date, String partOfDay) {
        return partOfDayForecastRepository.getByDateAndPartOfDay(date, partOfDay);
    }

    public List<PartOfDayForecast> getPartOfDayForecasts(String partOfDay, int limit) {
        Pageable pageable = (Pageable) PageRequest.of(0, limit);
        return partOfDayForecastRepository.getForecastsByPartOfDay(partOfDay, pageable);
    }

    public Optional<PlaceForecast> getPlaceForecast(LocalDate localDate, String partOfDay, String place) {
        return placeForecastRepository.getByDateAndPartOfDayAndName(localDate, partOfDay, place);
    }

    public Optional<PeipsiForecast> getPeipsiForecast(LocalDate localDate, String partOfDay) {
        return peipsiForecastRepository.getByDateAndPartOfDay(localDate, partOfDay);
    }

    public List<String> getAvailablePlacesByDate(LocalDate localDate) {
        return placeForecastRepository.getAllPlacesByDate(localDate);
    }
}
