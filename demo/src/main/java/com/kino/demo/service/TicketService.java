package com.kino.demo.service;

import com.kino.demo.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketService {
    Ticket getTicketById(long id);
    List<Ticket> getAllTickets();
    List<Ticket> getTicketsByUserId(long userId);
    void saveTicket(Ticket ticket);
    void updateTicket(Ticket ticket);
    Ticket deleteTicketByID(long id);
    void deleteTicketsByScreeningId(long screeningId);
    void deleteTicketsByUserId(long userId);

}
