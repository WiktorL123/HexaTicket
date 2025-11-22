package com.pjatk.mongo.adapter;

import com.pjatk.core.domain.Event;
import com.pjatk.core.port.out.EventsRepositoryPort;
import com.pjatk.mongo.model.EventDocument;
import com.pjatk.mongo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Component
public class EventMongoAdapter implements EventsRepositoryPort {

    private final EventRepository repository;
    @Override
    public Event create(Event event) {
        EventDocument doc = new EventDocument(
                null,
                event.getName(),
                event.getDescription(),
                event.getVenue(),
                LocalDateTime.now(),
                event.getTotalSeats(),
                event.getAvailableSeats(),
                event.getPrice(),
                event.getCategory(),
                event.getStatus()
        );

        EventDocument savedDoc = repository.save(doc);

        return new Event(
                savedDoc.getId(),
                savedDoc.getName(),
                doc.getDescription(),
                doc.getVenue(),
                doc.getStartDate(),
                doc.getTotalSeats(),
                doc.getAvailableSeats(),
                doc.getPrice(),
                doc.getCategory(),
                doc.getStatus());

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<Event> findAll() {
        return List.of();
    }

    @Override
    public Event getByEmail(String email) {
        return null;
    }
}
