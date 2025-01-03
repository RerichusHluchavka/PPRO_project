package com.kino.demo.service;

import com.kino.demo.model.Ticket;
import com.kino.demo.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket getTicketById(long id){
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getTicketsByUserId(long userId){
        return ticketRepository.findAllByUserId(userId);
    }

    @Override
    public void saveTicket(Ticket ticket){
        ticketRepository.save(ticket);
    }

    @Override
    public void updateTicket(Ticket ticket){
        Optional<Ticket> ticketDB = ticketRepository.findById(ticket.getId());
        if(ticketDB.isPresent()){
            ticketRepository.save(ticket);
        }
    }

    @Override
    public Ticket deleteTicketByID(long id){
        Optional<Ticket> ticketDB = ticketRepository.findById(id);
        if(ticketDB.isPresent()){
            Ticket ticket = ticketDB.get();
            ticketRepository.delete(ticket);
            return ticket;
        }
        return null;
    }

    @Transactional
    @Override
    public void deleteTicketsByScreeningId(long screeningId) {
        ticketRepository.deleteTicketByScreeningId(screeningId);
    }

    @Transactional
    @Override
    public void deleteTicketsByUserId(long userId) {
        ticketRepository.deleteTicketsByUserId(userId);
    }
}
