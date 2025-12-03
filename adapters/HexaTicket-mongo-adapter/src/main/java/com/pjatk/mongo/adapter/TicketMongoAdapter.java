package com.pjatk.mongo.adapter;

import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.ticket.Ticket;
import com.pjatk.core.port.out.EventsRepositoryPort;
import com.pjatk.core.port.out.TicketsRepositoryPort;
import com.pjatk.core.view.MyTicketView;
import com.pjatk.mongo.mapper.DomainMongoMapper;
import com.pjatk.mongo.model.EventDocument;
import com.pjatk.mongo.model.TicketDocument;
import com.pjatk.mongo.projection.MyTicketsProjection;
import com.pjatk.mongo.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TicketMongoAdapter implements TicketsRepositoryPort {
    private final TicketRepository repo;
    private final DomainMongoMapper mapper;
    private final MongoTemplate mongoTemplate;
    @Override
    public Ticket save(Ticket ticket) {

        TicketDocument ticketToSave = mapper.toTicketDocument(ticket);

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
        System.out.println("[DEBUG] Agregacja dla: " + email);

        Aggregation aggregation = Aggregation.newAggregation(
                // 1. MATCH: Filtrowanie po emailu
                Aggregation.match(Criteria.where("owner_email").is(email)),

                // 2. LOOKUP: Łączenie po poprawnych polach z bazy
                // localField: "event" (tak nazywa się pole w Twoim zrzucie z bazy)
                // foreignField: "_id"
                Aggregation.lookup("events", "event", "_id", "eventDetails"),

                // 3. UNWIND: Rozwijamy tablicę (true = nie usuwaj jak pusty, dla bezpieczeństwa)
                Aggregation.unwind("eventDetails", true),

                // 4. PROJECT: Mapowanie nazw pól Baza -> Java
                Aggregation.project()
                        .and("_id").as("id")
                        .and("owner_name").as("ownerName")
                        .and("owner_email").as("email")
                        .and("ticket_code").as("ticketCode")
                        .and("eventDetails").as("eventDetails")
        );

        AggregationResults<MyTicketsProjection> results = mongoTemplate.aggregate(
                aggregation,
                "tickets",
                MyTicketsProjection.class
        );

        // Debugowanie wyników
        List<MyTicketsProjection> mappedResults = results.getMappedResults();
        System.out.println("[DEBUG] Pobrane rekordy: " + mappedResults.size());
        if (!mappedResults.isEmpty() && mappedResults.get(0).getEventDetails() == null) {
            System.out.println("[ALARM] EventDetails jest null! Sprawdź czy ID w 'event' istnieje w kolekcji 'events'.");
        }

        return mappedResults.stream()
                .map(mapper::projectionToTicketView)
                .toList();
    }
}

