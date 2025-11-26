package com.pjatk.core.port.out;

import com.pjatk.core.domain.Ticket;

import java.util.List;

public interface TicketsRepositoryPort {
    Ticket create(Ticket ticket);

    void delete();

    List<Ticket> findALl();

    Ticket getByEmail(String email);
}
