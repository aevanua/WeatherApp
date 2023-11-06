package com.example.background.repositories;

import com.example.background.entities.PeipsiForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PeipsiForecastRepository extends JpaRepository<PeipsiForecast, Long> {
    @Query("SELECT p FROM PeipsiForecast p WHERE p.date = :date AND p.partOfDay = :partOfDay")
    Optional<PeipsiForecast> getByDateAndPartOfDay(@Param("date") LocalDate date, @Param("partOfDay") String partOfDay);
}