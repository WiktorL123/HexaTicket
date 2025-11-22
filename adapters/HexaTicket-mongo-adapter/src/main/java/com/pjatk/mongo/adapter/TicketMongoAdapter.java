package com.pjatk.mongo.adapter;

import com.pjatk.core.domain.Ticket;
import com.pjatk.core.port.out.TicketsRepositoryPort;
import com.pjatk.mongo.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@RequiredArgsConstructor
@Component
public class TicketMongoAdapter implements TicketsRepositoryPort {

    private final TicketRepository repository;


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
