package com.pjatk.core.port.out;

import com.pjatk.core.domain.Event;

import java.util.List;

public interface EventsRepositoryPort {
    Event create(Event event);

    void delete(String id);

    List<Event> findAll();

    Event getByEmail(String email);
}
