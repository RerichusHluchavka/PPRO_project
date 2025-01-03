package com.kino.demo.controller;

import com.kino.demo.model.Film;
import com.kino.demo.model.Screening;
import com.kino.demo.model.Ticket;
import com.kino.demo.model.User;
import com.kino.demo.repository.FilmRepository;
import com.kino.demo.repository.ScreeningRepository;
import com.kino.demo.repository.TicketRepository;
import com.kino.demo.repository.UserRepository;
import com.kino.demo.service.FilmService;
import com.kino.demo.service.ScreeningService;
import com.kino.demo.service.TicketService;
import com.kino.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private UserRepository userRepository;
    private UserService userService;
    private FilmService filmService;
    private FilmRepository filmRepository;
    private ScreeningService screeningService;
    private ScreeningRepository screeningRepository;
    private TicketService ticketService;
    private TicketRepository ticketRepository;

    public AdminController(FilmService filmService, FilmRepository filmRepository, ScreeningService screeningService, ScreeningRepository screeningRepository, TicketService ticketService, TicketRepository ticketRepository, UserRepository userRepository, UserService userService) {
        this.filmService = filmService;
        this.filmRepository = filmRepository;
        this.screeningService = screeningService;
        this.screeningRepository = screeningRepository;
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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

    @GetMapping("/userList")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/userList";
    }

    @GetMapping("/ticketList")
    public String ticketList(Model model) {
        model.addAttribute("tickets", ticketRepository.findAll());
        return "admin/ticketList";
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

    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable int id, String username, String password, Model model, RedirectAttributes redirectAttributes, Boolean role, Boolean change) {
        User user = userService.getUserById(id);
        user.setUsername(username);
        if(userRepository.findByUsername(user.getUsername()) != null ) {
            if(!user.getUsername().equals(userService.findByUsername(user.getUsername()).getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Username is already taken");
                return "redirect:/admin/editUser/" + id;
            }
        }
        if (role != null) {
            user.setRole("ADMIN");
        }
        if(change != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(password);
            user.setPassword(encryptedPassword);
        }

        userService.updateUser(user);
        return "redirect:/admin/userList";
    }

    @PostMapping("/editTicket/{id}")
    public String editTicket(@PathVariable int id, @Valid Ticket ticket, long user_id,long screening_id, RedirectAttributes redirectAttributes, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "redirect:/admin/editTicket/" + id;
        }
        ticket.setUser(userService.getUserById(user_id));
        ticket.setScreening(screeningService.getScreeningById(screening_id));
        ticketService.updateTicket(ticket);
        return "redirect:/admin/ticketList";
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

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/editUser";
    }

    @GetMapping("/editTicket/{id}")
    public String editTicket(@PathVariable long id, Model model) {
        model.addAttribute("ticket", ticketService.getTicketById(id));
        model.addAttribute("screenings", screeningService.getAllScreenings());
        model.addAttribute("users", userService.getAllUsers());

        return "admin/editTicket";
    }

    @GetMapping("/screeningDelete/{id}")
    public String screeningDelete(@PathVariable long id) {
        ticketService.deleteTicketsByScreeningId(id);
        screeningService.deleteScreeningById(id);
        return "redirect:/admin/screeningList";
    }

    @GetMapping("/filmDelete/{id}")
    public String deleteFilm(@PathVariable long id){
        screeningService.deleteScreeningsByFilmId(id);
        filmService.deleteFilmById(id);
        return "redirect:/admin/filmList";
    }

    @GetMapping("/userDelete/{id}")
    public String userDelete(@PathVariable long id){
        ticketService.deleteTicketsByUserId(id);
        userService.deleteUserByID(id);
        return "redirect:/admin/userList";
    }

    @GetMapping("/ticketDelete/{id}")
    public String ticketDelete(@PathVariable long id){
        ticketService.deleteTicketByID(id);
        return "redirect:/admin/ticketList";
    }



}
