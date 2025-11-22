package com.pjatk.application.service;

import com.pjatk.core.domain.Ticket;
import com.pjatk.core.port.in.TicketsPort;
import com.pjatk.core.port.out.TicketsRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TicketService implements TicketsPort {

    private final TicketsRepositoryPort port;
    @Override
    public Ticket create(Ticket ticket) {
        return null;
    }

    @Override
    public void delete() {

    }

    @Override
    public List<Ticket> findALl() {
        return List.of();
    }

    @Override
    public Ticket getById(String id) {
        return null;
    }
}
