package com.pjatk.mongo.adapter;

import com.pjatk.core.domain.Event;
import com.pjatk.core.port.out.EventsRepositoryPort;
import com.pjatk.mongo.mapper.DomainMongoMapper;
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
    private final DomainMongoMapper mapper;
    @Override
    public Event create(Event event) {
        EventDocument doc = mapper.toEventDocument(event);
        EventDocument savedDoc = repository.save(doc);
        return mapper.toEventDomain(savedDoc);
    }
    @Override
    public void delete(String id) {

    }

    @Override
    public List<Event> findAll() {
        List<EventDocument> allEvents = repository.findAll();
        List<Event> domainEvents = allEvents
                .stream()
                .map(event->mapper.toEventDomain(event))
                .toList();
        return domainEvents;
    }

    @Override
    public Event getByEmail(String email) {
        return null;
    }
}
