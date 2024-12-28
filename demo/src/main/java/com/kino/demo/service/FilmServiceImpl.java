package com.kino.demo.service;

import com.kino.demo.model.Film;
import com.kino.demo.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Film deleteFilmById(long id){
        Optional<Film> filmDB = filmRepository.findById(id);
        if(filmDB.isPresent()){
            Film film = filmDB.get();
            filmRepository.delete(film);
            return film;
        }
        return null;
    }

    @Override
    public void updateFilm(Film film) {
        Optional<Film> filmDB = filmRepository.findById(film.getId());
        if(filmDB.isPresent()){
            filmRepository.save(film);
        }
    }
}
