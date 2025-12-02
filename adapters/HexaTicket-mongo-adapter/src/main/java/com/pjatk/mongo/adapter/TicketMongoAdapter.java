package com.pjatk.mongo.adapter;

import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.ticket.Ticket;
import com.pjatk.core.exception.NotFoundException;
import com.pjatk.core.port.out.EventsRepositoryPort;
import com.pjatk.core.port.out.TicketsRepositoryPort;
import com.pjatk.core.view.MyTicketView;
import com.pjatk.mongo.mapper.DomainMongoMapper;
import com.pjatk.mongo.model.EventDocument;
import com.pjatk.mongo.model.TicketDocument;
import com.pjatk.mongo.projection.MyTicketsProjection;
import com.pjatk.mongo.repository.EventRepository;
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
    private final EventRepository eventRepo;
    @Override
    public Ticket save(Ticket ticket) {
        EventDocument eventDocument = eventRepo.findById(ticket.getEventId())
                .orElseThrow(() -> new NotFoundException("event not found"));
        TicketDocument ticketToSave = mapper.toTicketDocument(ticket, eventDocument);
        TicketDocument savedTicket = repo.save(ticketToSave);
        return mapper.toTicketDomain(savedTicket);

    }



    @Override
    public Optional<Ticket> findById(String ticketId) {
        return repo.findById(ticketId)
                .map(doc->mapper.toTicketDomain(doc));
    }

    @Override
    public List<MyTicketView> myTickets(String email) {
        List<TicketDocument> tickets = repo.findByOwnerEmail(email);
        return tickets
                .stream()
                .map(doc -> mapper.projectionToTicketView(doc))
                .toList();
    }
}

