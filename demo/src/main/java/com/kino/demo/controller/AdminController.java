package com.kino.demo.controller;

import com.kino.demo.model.Film;
import com.kino.demo.model.Screening;
import com.kino.demo.repository.FilmRepository;
import com.kino.demo.repository.ScreeningRepository;
import com.kino.demo.service.FilmService;
import com.kino.demo.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private FilmService filmService;
    private FilmRepository filmRepository;
    private ScreeningService screeningService;
    private ScreeningRepository screeningRepository;

    public AdminController(FilmService filmService, FilmRepository filmRepository, ScreeningService screeningService, ScreeningRepository screeningRepository) {
        this.filmService = filmService;
        this.filmRepository = filmRepository;
        this.screeningService = screeningService;
        this.screeningRepository = screeningRepository;
    }

    @GetMapping("/administration")
    public String administration() {
        return "admin/administration"; // Renders the "admin/administration" view
    }

    @GetMapping("/createFilm")
    public String createFilm() {
        return "admin/createFilm"; // Renders the "admin/createFilm" view
    }

    @GetMapping("/createScreening")
    public String createScreening(Model model) {
        model.addAttribute("films", filmService.getAllFilms());
        return "admin/createScreening";
    }

    @GetMapping("/filmList")
    public String filmList(Model model) {
        model.addAttribute("films", filmService.getAllFilms());
        return "admin/filmList"; // Renders the "admin/createFilm" view
    }

    @GetMapping("/screeningList")
    public String screeningList(Model model) {
        model.addAttribute("screenings", screeningRepository.findAll());
        return "admin/screeningList";
    }

    @PostMapping("/createScreening")
    public String createScreening(long filmId, LocalDateTime screeningDateTime, Model model) {

        if (filmRepository.findById(filmId).isEmpty()) {
            return "admin/createScreening";
        }
        Screening screening = new Screening();
        screening.setScreeningDateTime(screeningDateTime);
        screening.setFilm(filmService.getFilmById(filmId));

        LocalDateTime from = screening.getScreeningDateTime();
        LocalDateTime to = screening.getScreeningDateTime().plusMinutes(screening.getFilm().getLength());
        // 600 is maximum film length
        LocalDateTime before = screening.getScreeningDateTime().minusMinutes(600);

        if (screeningRepository.findScreeningsByScreeningDateTimeBetween(from, to).size() > 0) {
            model.addAttribute("errorMessage", "A screening already exists on that timeframe");
            model.addAttribute("films", filmService.getAllFilms());
            return "admin/createScreening";
        }

        if (screeningRepository.findScreeningsByScreeningDateTimeBetween(before, from).size() > 0) {
            List<Screening> screenings = screeningRepository.findScreeningsByScreeningDateTimeBetween(before, from);
            for (Screening screening1 : screenings) {
                if (screening1.getScreeningDateTime().plusMinutes(screening1.getFilm().getLength()).isAfter(screening.getScreeningDateTime())) {
                    model.addAttribute("errorMessage", "A screening already exists on that timeframe");
                    model.addAttribute("films", filmService.getAllFilms());
                    return "admin/createScreening";
                }
            }
        }

        System.out.println(screening.getFilm().getId());
        screeningRepository.save(screening);
        return "redirect:/admin/screeningList";
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

    //Added Valid if it brakes delete it
    @PostMapping("/editFilm/{id}")
    public String editPostFilm(@PathVariable int id, @Valid Film film, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "admin/editFilm/" + id;
        }
        if(filmRepository.findByName(film.getName()) != null) {
            if(filmRepository.findByName(film.getName()).getId() != id)
            return "admin/editFilm/" + id;
        }
        filmService.updateFilm(film);
        return "redirect:/admin/filmList";
    }

    @PostMapping("/editScreening/{id}")
    public String editScreening(@PathVariable int id, long filmId, LocalDateTime screeningDateTime, Model model, RedirectAttributes redirectAttributes) {

        Screening screening = new Screening();
        screening.setScreeningDateTime(screeningDateTime);
        screening.setFilm(filmService.getFilmById(filmId));
        screening.setId(id);

        LocalDateTime from = screening.getScreeningDateTime();
        LocalDateTime to = screening.getScreeningDateTime().plusMinutes(screening.getFilm().getLength());
        // 600 is maximum film length
        LocalDateTime before = screening.getScreeningDateTime().minusMinutes(600);

        if (screeningRepository.findScreeningsByScreeningDateTimeBetween(from, to).size() > 0) {
            List<Screening> screeningList = screeningRepository.findScreeningsByScreeningDateTimeBetween(from, to);
            if(screeningList.size() == 1) {
                if(screening.getId() != screeningList.getFirst().getId()) {
                        redirectAttributes.addFlashAttribute("errorMessage", "A screening already exists on that timeframe.");
                        return "redirect:/admin/editScreening/" + id;
                }
                if (screeningRepository.findScreeningsByScreeningDateTimeBetween(from, to).size() > 1) {
                    redirectAttributes.addFlashAttribute("errorMessage", "A screening already exists on that timeframe.");

                    return "redirect:/admin/editScreening/" + id;
                }
            }
        }
        if (screeningRepository.findScreeningsByScreeningDateTimeBetween(before, from).size() > 0) {
            List<Screening> screenings = screeningRepository.findScreeningsByScreeningDateTimeBetween(before, from);
            for (Screening screening1 : screenings) {
                if (screening1.getScreeningDateTime().plusMinutes(screening1.getFilm().getLength()).isAfter(screening.getScreeningDateTime()) && (screening.getId() != screening1.getId())) {
                    redirectAttributes.addFlashAttribute("errorMessage", "A screening already exists on that timeframe.");
                    return "redirect:/admin/editScreening/" + id;
                }
            }
        }

        screeningService.updateScreening(screening);
        return "redirect:/admin/screeningList";

    }

    @GetMapping("/editFilm/{id}")
    public String editFilm(@PathVariable int id, Model model) {
        model.addAttribute("film", filmService.getFilmById(id));
        return "admin/editFilm";
    }

    @GetMapping("/editScreening/{id}")
    public String editScreening(@PathVariable int id, Model model) {
        model.addAttribute("screening", screeningService.getScreeningById(id));
        model.addAttribute("films", filmService.getAllFilms());
        return "admin/editScreening";
    }

    @GetMapping("/screeningDelete/{id}")
    public String screeningDelete(@PathVariable int id) {
        screeningService.deleteScreeningById(id);
        return "redirect:/admin/screeningList";
    }

    @GetMapping("/filmDelete/{id}")
    public String deleteFilm(@PathVariable long id){
        filmService.deleteFilmById(id);
        return "redirect:/admin/filmList";
    }

}
