package com.kino.demo.controller;

import com.kino.demo.model.Screening;
import com.kino.demo.model.Ticket;
import com.kino.demo.model.User;
import com.kino.demo.repository.ScreeningRepository;
import com.kino.demo.repository.TicketRepository;
import com.kino.demo.repository.UserRepository;
import com.kino.demo.service.ScreeningService;
import com.kino.demo.service.TicketService;
import com.kino.demo.service.UserService;
import jakarta.jws.WebParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private TicketService ticketService;
    private TicketRepository ticketRepository;
    private UserService userService;
    private UserRepository userRepository;
    private ScreeningService screeningService;
    private ScreeningRepository screeningRepository;

    public TicketController(TicketService ticketService, TicketRepository ticketRepository, UserService userService, UserRepository userRepository, ScreeningService screeningService, ScreeningRepository screeningRepository) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.screeningService = screeningService;
        this.screeningRepository = screeningRepository;
    }


    @GetMapping("/reservation/{screeningId}")
    public String reservation(Model model,@PathVariable long screeningId) {
        Screening screening = screeningService.getScreeningById(screeningId);
        if(screening != null) {
            model.addAttribute("screening", screening);
            return "ticket/reservation";
        }
        return "redirect:/";
    }

    @PostMapping("/reservation/{screeningId}")
    public String reservation(@PathVariable long screeningId, int ticketNumber) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        Ticket ticket = new Ticket();
        ticket.setCount(ticketNumber);
        ticket.setUser(user);
        ticket.setScreening(screeningService.getScreeningById(screeningId));
        ticketService.saveTicket(ticket);
        return "redirect:/ticket/detail/" + ticket.getId();
    }

    @GetMapping("/detail/{ticketId}")
    public String detail(@PathVariable long ticketId, Model model) {
        Ticket ticket = ticketService.getTicketById(ticketId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());


        if(!Objects.equals(user, ticket.getUser())) {
            if(!Objects.equals(user.getRole(), "ADMIN")){
                return "redirect:/";
            }
        }

        model.addAttribute("ticket", ticket);
        return "ticket/detail";
    }

    @GetMapping("/myTickets")
    public String myTickets(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());
        model.addAttribute("tickets", ticketService.getTicketsByUserId(user.getId()));
        return "ticket/myTickets";
    }
}
