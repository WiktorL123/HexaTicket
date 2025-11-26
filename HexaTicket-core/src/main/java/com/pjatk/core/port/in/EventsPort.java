package com.pjatk.core.port.in;

import com.pjatk.core.domain.Event;

import java.util.List;
import java.util.Optional;

public interface EventsPort {
    Event create(Event event);

    void delete(String id);

    List<Event> findAll();

    Event getById(String id);

    Event updatePartially(String id, Event event);
}
