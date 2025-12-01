package com.pjatk.application.service;

import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.event.EventStatus;
import com.pjatk.core.exception.NotFoundException;
import com.pjatk.core.exception.TooEarlyDateException;
import com.pjatk.core.exception.TooMuchSeatsException;
import com.pjatk.core.port.in.EventsPort;
import com.pjatk.core.port.out.EventsRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class EventService implements EventsPort {

    private final EventsRepositoryPort port;

    @Override
    public Event getById(String id) {
       return port.getById(id).
               orElseThrow(()->new NotFoundException("Event not found"));

    }

    @Override
    public Event create(Event event) {
        event.setEventStatus(EventStatus.ACTIVE);
        if (event.getStartDate().isBefore(LocalDateTime.now().plusHours(24))){
            throw new TooEarlyDateException("Event must be at least 24h before start");
        }
        event.setAvailableSeats(event.getTotalSeats());

        return port.save(event);
    }

    @Override
    public void delete(String id) {
        Event event = getById(id);
        port.delete(id);
    }

    @Override
    public List<Event> findAll(int page, int size, String category, LocalDateTime startDate) {
        return port.findAll(page, size, category, startDate);
    }

    @Override
    public Event updatePartially(String id, Event event) {
        System.out.println("trying to set new values: " + event);

        Event existingEvent = port.getById(id).orElseThrow(()->new NotFoundException("Event not found"));

        if (event.getName() != null) {
            existingEvent.setName(event.getName());
        }

        if (event.getDescription() != null) {
            existingEvent.setDescription(event.getDescription());
        }

        if (event.getPrice() != null) {
            existingEvent.setPrice(event.getPrice());
        }

        if (event.getVenue() != null) {
            existingEvent.setVenue(event.getVenue());
        }

        if (event.getCategory() != null) {
            existingEvent.setCategory(event.getCategory());
        }

        if (event.getTotalSeats() != null) {
           updateTotalSeatsLogic(existingEvent, event.getTotalSeats());
        }


        if (event.getAvailableSeats() != null) {
            if (event.getAvailableSeats() > existingEvent.getTotalSeats()){
                throw new TooMuchSeatsException("Available seats number cannot be bigger than total seats");
            }
            existingEvent.setAvailableSeats(event.getAvailableSeats());
        }

        if (event.getStartDate() != null) {
            //event = port.findByDate
            //jesli istnieje event z ta data->wyjatek
            existingEvent.setStartDate(event.getStartDate());
        }
        if (event.getEventStatus() != null) {
            existingEvent.setEventStatus(event.getEventStatus());
        }
        return port.save(existingEvent);
    }

    private void updateTotalSeatsLogic(Event existingEvent, int newTotalSeats) {
        int currentlyReserved = existingEvent.getTotalSeats() - existingEvent.getAvailableSeats();

        if (newTotalSeats < currentlyReserved) {
            throw new TooMuchSeatsException(
                    String.format("Nie można zmniejszyć liczby miejsc do %d. Sprzedano już %d biletów.",
                            newTotalSeats, currentlyReserved)
            );
        }

        int newAvailable = newTotalSeats - currentlyReserved;

        existingEvent.setTotalSeats(newTotalSeats);
        existingEvent.setAvailableSeats(newAvailable);
    }
}
