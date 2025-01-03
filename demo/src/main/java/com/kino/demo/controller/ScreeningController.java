package com.kino.demo.controller;

import com.kino.demo.model.Film;
import com.kino.demo.model.Screening;
import com.kino.demo.repository.ScreeningRepository;
import com.kino.demo.service.ScreeningService;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/screening")
public class ScreeningController {
    private ScreeningService screeningService;
    private ScreeningRepository screeningRepository;

    public ScreeningController(ScreeningService screeningService, ScreeningRepository screeningRepository) {
        this.screeningService = screeningService;
        this.screeningRepository = screeningRepository;
    }

    @GetMapping("/screenings")
    public String screenings(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model, Sort sort) {
        LocalDate baseDate = (date != null) ? date : LocalDate.now();
        Map<LocalDate, List<Screening>> screeningsByDate = new HashMap<>();

        LocalDate startOfWeek = baseDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = baseDate.with(java.time.DayOfWeek.SUNDAY);

        List<Screening> screenings = screeningRepository.findScreeningsByScreeningDateTimeBetween(
                startOfWeek.atStartOfDay(),
                endOfWeek.atTime(23,55)
        );

        for (int i = 0; i < 7; i++) {
            screeningsByDate.computeIfAbsent(startOfWeek.plusDays(i), k -> new ArrayList<>());
        }

        for (Screening screening : screenings) {
            LocalDate screeningDate = screening.getScreeningDateTime().toLocalDate();
            screeningsByDate
                    .computeIfAbsent(screeningDate, k -> new ArrayList<>())
                    .add(screening);
        }


        model.addAttribute("screeningsByDate", screeningsByDate);
        model.addAttribute("startOfWeek", startOfWeek);
        model.addAttribute("screenings", screenings);
        return "screening/screenings";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id){
        Screening screening = screeningService.getScreeningById(id);
        if(screening != null) {
            System.out.println(screening.getScreeningDateTime());
            model.addAttribute("screening", screening);
            return "screening/detail";
        }
        System.out.println(id);
        return "redirect:/";
    }

}
