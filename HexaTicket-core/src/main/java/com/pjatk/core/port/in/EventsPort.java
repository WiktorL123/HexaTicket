package com.pjatk.core.port.in;

import com.pjatk.core.domain.Event;

import java.util.List;

public interface EventsPort {
    Event create(Event event);

    void delete(String id);

    List<Event> findAll();

    Event getByEmail(String email);
}
