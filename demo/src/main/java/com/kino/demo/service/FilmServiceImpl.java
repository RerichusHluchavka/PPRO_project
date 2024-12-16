package com.kino.demo.service;

import com.kino.demo.model.Film;
import com.kino.demo.model.User;
import com.kino.demo.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;

    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Film findByName(String name) {
        return filmRepository.findByName(name);
    }

    @Override
    public Film getFilmById(long id){
        return filmRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Film film) {
        filmRepository.save(film);
    }
}
