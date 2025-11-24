package com.pjatk.application.service;

import com.pjatk.core.domain.Event;
import com.pjatk.core.port.in.EventsPort;
import com.pjatk.core.port.out.EventsRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class EventService implements EventsPort {

    private final EventsRepositoryPort port;

    @Override
    public Event create(Event event) {
        return port.create(event);
    }

    @Override
    public void delete(String id) {

    }
    @Override
    public List<Event> findAll() {
        return port.findAll();
    }

    @Override
    public Event getByEmail(String email) {
        return null;
    }
}
