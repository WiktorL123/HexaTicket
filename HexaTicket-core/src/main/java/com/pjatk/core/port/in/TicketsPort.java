package com.pjatk.core.port.in;

import com.pjatk.core.domain.Ticket;

import java.util.List;

public interface TicketsPort {

    Ticket create(Ticket ticket);

    void delete();

    List<Ticket> findALl();

    Ticket getById(String id);
}
