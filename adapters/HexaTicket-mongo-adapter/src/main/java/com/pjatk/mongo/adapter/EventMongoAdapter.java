package com.pjatk.mongo.adapter;

import com.pjatk.core.domain.Event;
import com.pjatk.core.exception.NotFoundException;
import com.pjatk.core.port.out.EventsRepositoryPort;
import com.pjatk.mongo.mapper.DomainMongoMapper;
import com.pjatk.mongo.model.EventDocument;
import com.pjatk.mongo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class EventMongoAdapter implements EventsRepositoryPort {

    private final EventRepository repository;
    private final DomainMongoMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Event> getById(String id) {
        return repository.findById(id)
                .map(e->mapper.toEventDomain(e));

    }

    @Override
    public Event save(Event event) {
        EventDocument doc = mapper.toEventDocument(event);
        EventDocument savedDoc = repository.save(doc);
        return mapper.toEventDomain(savedDoc);
    }
    @Override
    public void delete(String id) {
        EventDocument event = repository.findById(id)
                .orElseThrow(()->new NotFoundException("event not found"));
        repository.delete(event);
    }

//    @Override
//    public List<Event> findAll() {
//        List<EventDocument> allEvents = repository.findAll();
//        List<Event> domainEvents = allEvents
//                .stream()
//                .map(event->mapper.toEventDomain(event))
//                .toList();
//        return domainEvents;
//    }


    // W module mongo-adapter/EventMongoAdapter.java
    @Override
    public List<Event> findAll(int page, int size, String category, LocalDateTime startDate) {
        List<Criteria> criteria = new ArrayList<>();

        if (category != null && !category.isEmpty()){
            criteria.add(Criteria.where("category").is(category));
        }

        if (startDate != null){
            criteria.add(Criteria.where("startDate").gte(startDate));
        }

        Query query = new Query();
        if (!criteria.isEmpty()){
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));

        }


        PageRequest pageRequest = PageRequest.of(page, size);
        query.with(pageRequest);
        List<EventDocument> filteredDocs = mongoTemplate.find(query, EventDocument.class, "events");
        return filteredDocs
                .stream()
                .map(document -> mapper.toEventDomain(document))
                .toList();
    }

    @Override
    public Event updatePartially(String id, Event event) {
        return null;
    }
}
