package com.example.background.repositories;

import com.example.background.entities.PlaceForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaceForecastRepository extends JpaRepository<PlaceForecast, Long> {
    @Query("SELECT p FROM PlaceForecast p WHERE p.date = :date AND p.partOfDay = :partOfDay")
    Optional<PlaceForecast> getByDateAndPartOfDay(@Param("date") LocalDate date, @Param("partOfDay") String partOfDay);

    @Query("SELECT DISTINCT p.name FROM PlaceForecast p WHERE p.date = :date AND p.partOfDay = 'night'")
    List<String> getAllPlacesByDate(@Param("date") LocalDate date);

    @Query("SELECT p FROM PlaceForecast p WHERE p.date = :date AND p.partOfDay = :partOfDay AND p.name = :name")
    Optional<PlaceForecast> getByDateAndPartOfDayAndName(@Param("date") LocalDate date, @Param("partOfDay") String partOfDay, @Param("name") String name);
}