package com.pjatk.core.port.out;

import com.pjatk.core.domain.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventsRepositoryPort {
    Event save(Event event);

    void delete(String id);

    List<Event> findAll(int page, int size, String category, LocalDateTime startDate);

    Optional<Event> getById(String id);

    Event updatePartially(String id, Event event);
}
