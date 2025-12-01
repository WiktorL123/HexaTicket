package com.pjatk.core.port.out;

import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.ticket.Ticket;
import com.pjatk.core.view.MyTicketView;

import java.util.List;
import java.util.Optional;

public interface TicketsRepositoryPort {
    Ticket save(Ticket ticket);

    Optional<Ticket> findById(String ticketId);

    List<MyTicketView> myTickets(String email);
}
