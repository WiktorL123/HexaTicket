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
        System.out.println("[DEBUG] Szukam biletów dla: " + email);

        Aggregation aggregation = Aggregation.newAggregation(
                // 1. MATCH
                Aggregation.match(Criteria.where("owner_email").is(email)),

                // 2. LOOKUP (Bez konwersji! Używamy pola 'event')
                // 'event' to nazwa pola w Twoim dokumencie w bazie
                Aggregation.lookup("events", "event", "_id", "eventDetails"),

                // 3. UNWIND
                Aggregation.unwind("eventDetails", true),

                // 4. PROJECT
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

        List<MyTicketsProjection> mappedResults = results.getMappedResults();

        // Debug
        if (!mappedResults.isEmpty()) {
            System.out.println("Znalazlem eventDetails: " + (mappedResults.get(0).getEventDetails() != null));
        }

        return mappedResults.stream()
                .map(mapper::projectionToTicketView)
                .toList();
    }
}

