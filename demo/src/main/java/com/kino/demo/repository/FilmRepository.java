package com.kino.demo.repository;

import com.kino.demo.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
    Film findByName(String name);
}
