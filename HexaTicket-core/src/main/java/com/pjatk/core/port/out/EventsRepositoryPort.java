package com.pjatk.core.port.out;

import com.pjatk.core.domain.Event;

import java.util.List;
import java.util.Optional;

public interface EventsRepositoryPort {
    Event save(Event event);

    void delete(String id);

    List<Event> findAll();

    Optional<Event> getById(String id);

    Event updatePartially(String id, Event event);
}
