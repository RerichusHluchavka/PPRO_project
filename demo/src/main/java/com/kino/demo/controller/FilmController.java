package com.kino.demo.controller;

import com.kino.demo.model.Film;
import com.kino.demo.repository.FilmRepository;
import com.kino.demo.service.FilmService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/film")
public class FilmController {
    private FilmService filmService;
    private FilmRepository filmRepository;

    public FilmController(FilmService filmService, FilmRepository filmRepository) {
        this.filmService = filmService;
        this.filmRepository = filmRepository;
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id){
        Film film = filmService.getFilmById(id);
        if(film != null){
            model.addAttribute("film", film);
            return "film_detail";
        }
        return "redirect:/film/";
    }
}
