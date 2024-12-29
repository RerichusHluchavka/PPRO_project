package com.kino.demo.repository;

import com.kino.demo.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findScreeningsByScreeningDateTimeBetween(LocalDateTime from, LocalDateTime to);
}
