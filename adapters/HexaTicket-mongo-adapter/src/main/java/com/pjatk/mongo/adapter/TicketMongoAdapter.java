package com.pjatk.mongo.adapter;

import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.ticket.Ticket;
import com.pjatk.core.port.out.TicketsRepositoryPort;
import com.pjatk.core.view.MyTicketView;
import com.pjatk.mongo.mapper.DomainMongoMapper;
import com.pjatk.mongo.model.TicketDocument;
import com.pjatk.mongo.projection.MyTicketsProjection;
import com.pjatk.mongo.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TicketMongoAdapter implements TicketsRepositoryPort {
    private final TicketRepository repo;
    private final DomainMongoMapper mapper;
    @Override
    public Ticket save(Ticket ticket) {
        TicketDocument ticketToSave = mapper.toTicketDocument(ticket);
        TicketDocument savedTicket =  repo.save(ticketToSave);
        return mapper.toTicketDomain(savedTicket);

    }



    @Override
    public Optional<Ticket> findById(String ticketId) {
        return repo.findById(ticketId)
                .map(doc->mapper.toTicketDomain(doc));
    }

    @Override
    public List<MyTicketView> myTickets(String email) {
        List<MyTicketsProjection> tickets = repo.findMyTicketsByOwnerEmailAggregation(email);
        return tickets
                .stream()
                .map(proj->mapper.projectionToTicketView(proj))
                .toList();
    }
}

