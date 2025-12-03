package com.pjatk.core.port.in;

import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.ticket.Ticket;
import com.pjatk.core.view.MyTicketView;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsPort {
    Event create(Event event);
    void update(Event event);
    void delete(String id);
    List<Event> findAll(int page, int size, String category, LocalDateTime startDate);
    Event getById(String id);

    Event updatePartially(String id, Event event);
}
