package com.kino.demo.service;

import com.kino.demo.model.Screening;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScreeningService {
    List<Screening> getAllScreenings();
    Screening getScreeningById(long id);
    void createScreening(Screening screening);
    Screening deleteScreeningById(long id);
    void updateScreening(Screening screening);
    void deleteScreeningsByFilmId(long filmId);
}
