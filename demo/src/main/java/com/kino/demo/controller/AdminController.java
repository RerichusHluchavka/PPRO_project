package com.kino.demo.controller;

import com.kino.demo.model.Film;
import com.kino.demo.repository.FilmRepository;
import com.kino.demo.service.FilmService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private FilmService filmService;
    private FilmRepository filmRepository;

    public AdminController(FilmService filmService, FilmRepository filmRepository) {
        this.filmService = filmService;
        this.filmRepository = filmRepository;
    }

    @GetMapping("/administration")
    public String administration() {
        return "admin/administration"; // Renders the "admin/administration" view
    }

    @GetMapping("/createFilm")
    public String createFilm() {
        return "admin/createFilm"; // Renders the "admin/createFilm" view
    }
    @GetMapping("/filmList")
    public String filmList() {
        return "admin/filmList"; // Renders the "admin/createFilm" view
    }


    @PostMapping("/createFilm")
    public String createFilm(@Valid Film film, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "admin/createFilm";
        }
        if(filmRepository.findByName(film.getName()) != null) {
            return "admin/createFilm";
        }
        filmService.save(film);
        return "redirect:/admin/administration";
    }
}
