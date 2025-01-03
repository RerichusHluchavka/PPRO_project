package com.kino.demo.repository;

import com.kino.demo.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByUserId(Long userId);
    void deleteTicketByScreeningId(Long screeningId);
}
