package com.kino.demo.controller;

import com.kino.demo.model.Film;
import com.kino.demo.repository.FilmRepository;
import com.kino.demo.service.FilmService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

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
    public String filmList(Model model) {
        model.addAttribute("films", filmService.getAllFilms());
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
        return "redirect:/admin/filmList";
    }

    @PostMapping("/editFilm/{id}")
    public String editPostFilm(@PathVariable int id, Film film, BindingResult bindingResult) {
        System.out.println(id);
        if(bindingResult.hasErrors()) {
            return "admin/editFilm/" + id;
        }
        if(filmRepository.findByName(film.getName()) != null) {
            if(filmRepository.findByName(film.getName()).getId() != id)
            return "admin/editFilm" + id;
        }
        filmService.updateFilm(film);
        return "redirect:/admin/filmList";
    }

    @GetMapping("/editFilm/{id}")
    public String editFilm(@PathVariable int id, Model model) {
        model.addAttribute("film", filmService.getFilmById(id));
        return "admin/editFilm";
    }



    @GetMapping("/filmDelete/{id}")
    public String deleteFilm(@PathVariable long id){
        filmService.deleteFilmById(id);
        System.out.println("Film deleted");
        return "redirect:/admin/filmList";
    }

}
