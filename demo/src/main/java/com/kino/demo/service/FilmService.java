package com.kino.demo.service;

import com.kino.demo.model.Film;
import org.springframework.stereotype.Service;

@Service
public interface FilmService {
    Film findByName(String name);
    Film getFilmById(long id);
    void save(Film film);
}
