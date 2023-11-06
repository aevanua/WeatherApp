package com.example.background.repositories;

import com.example.background.entities.PartOfDayForecast;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PartOfDayForecastRepository extends JpaRepository<PartOfDayForecast, Long> {

    @Query("SELECT p FROM PartOfDayForecast p WHERE p.partOfDay = :partOfDay")
    List<PartOfDayForecast> getForecastsByPartOfDay(@Param("partOfDay") String partOfDay, Pageable pageable);

    @Query("SELECT p FROM PartOfDayForecast p WHERE p.date = :date AND p.partOfDay = :partOfDay")
    Optional<PartOfDayForecast> getByDateAndPartOfDay(@Param("date") LocalDate date, @Param("partOfDay") String partOfDay);
}
