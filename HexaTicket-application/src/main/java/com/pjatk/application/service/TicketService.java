package com.pjatk.application.service;

import com.pjatk.core.command.BookTicketCommand;
import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.event.EventStatus;
import com.pjatk.core.domain.ticket.Ticket;
import com.pjatk.core.domain.ticket.TicketStatus;
import com.pjatk.core.exception.NotFoundException;
import com.pjatk.core.exception.TicketCannotBeBookedException;
import com.pjatk.core.exception.TooMuchSeatsException;
import com.pjatk.core.port.in.EventsPort;
import com.pjatk.core.port.in.TicketsPort;
import com.pjatk.core.port.out.NotificationPort;
import com.pjatk.core.port.out.TicketsRepositoryPort;
import com.pjatk.core.view.MyTicketView;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TicketService implements TicketsPort {
private final TicketsRepositoryPort ticketsRepositoryPort;
private final EventsPort eventsINPort;
private final NotificationPort notificationPort;



    @Override
    public Ticket bookTicket(BookTicketCommand command) {
        Event foundEvent = eventsINPort.getById(command.eventId());//adapter rzuca not found
        String ticketCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (foundEvent.getEventStatus().equals(EventStatus.CANCELLED) ||
                foundEvent.getEventStatus().equals(EventStatus.SOLD_OUT)){
            throw new TicketCannotBeBookedException("cannot book ticket for event with" + foundEvent.getEventStatus() + "status");
        }
        if (foundEvent.getAvailableSeats() <= 0){
            throw new TooMuchSeatsException("All seats have been reserved");
        }

        Ticket ticketToSave = new Ticket(
                null,
                command.eventId(),
                ticketCode,
                command.ownerEmail(),
                command.ownerName(),
                LocalDateTime.now(),
                TicketStatus.SOLD
        );
        foundEvent.setAvailableSeats(foundEvent.getAvailableSeats() -1);

        eventsINPort.create(foundEvent);
        notificationPort.sendEmail(ticketToSave.getOwnerEmail());//MOCK NA RAZIE
        return ticketsRepositoryPort.save(ticketToSave);
    }

    @Override
    public void cancelTicket(String ticketId) {
        Ticket ticket = ticketsRepositoryPort.findById(ticketId)
                .orElseThrow(()->new NotFoundException("ticket not found"));
        ticket.setStatus(TicketStatus.CANCELLED);

        Event eventToCancel = eventsINPort.getById(ticket.getEventId());
        eventToCancel.setAvailableSeats(eventToCancel.getAvailableSeats() + 1);
        eventsINPort.create(eventToCancel);

        notificationPort.sendEmail(ticket.getOwnerEmail());

        ticketsRepositoryPort.save(ticket);
    }



    @Override
    public List<MyTicketView> myTickets(String email) {
        System.out.println("service:  " + ticketsRepositoryPort.myTickets(email));
        return ticketsRepositoryPort.myTickets(email);
    }
}
