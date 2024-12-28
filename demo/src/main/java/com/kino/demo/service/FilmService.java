package com.kino.demo.service;

import com.kino.demo.model.Film;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FilmService {
    Film findByName(String name);
    Film getFilmById(long id);
    List<Film> getAllFilms();
    void save(Film film);
    Film deleteFilmById(long id);
    void updateFilm(Film film);
}
