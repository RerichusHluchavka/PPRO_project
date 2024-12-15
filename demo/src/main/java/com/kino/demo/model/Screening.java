package com.kino.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "screening")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private LocalDateTime screeningDateTime;

    @ManyToOne()
    @JoinColumn(name = "film_id")
    private Film film;

    public Screening() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull LocalDateTime getScreeningDateTime() {
        return screeningDateTime;
    }

    public void setScreeningDateTime(@NotNull LocalDateTime screeningDateTime) {
        this.screeningDateTime = screeningDateTime;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
}
