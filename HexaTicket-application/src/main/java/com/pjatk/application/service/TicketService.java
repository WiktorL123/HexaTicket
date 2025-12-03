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
                foundEvent.getEventStatus().equals(EventStatus.SOLD_OUT)) {
            throw new TicketCannotBeBookedException("cannot book ticket for event with" + foundEvent.getEventStatus() + "status");
        }
        if (foundEvent.getAvailableSeats() <= 0) {
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
        System.out.println("avaliable seats 1: " + foundEvent.getAvailableSeats());
        foundEvent.setAvailableSeats(foundEvent.getAvailableSeats() - 1);//ewidetnie odejmuje
        System.out.println("available seats 2: " + foundEvent.getAvailableSeats());
        System.out.println("saving updated: " + foundEvent.getId());
        eventsINPort.update(foundEvent);//zapisuje

        String body = "You have booked your ticket for eventId: " +
                 foundEvent.getName()
                + ". Have fun on the event! If it is a mistake, please contact us";
        String subject = "Your ticket has been booked";

        notificationPort.sendNotification(new NotificationPort.NotificationDetails(ticketToSave.getOwnerEmail(), subject, body));

        return ticketsRepositoryPort.save(ticketToSave);
    }

    @Override
    public void cancelTicket(String ticketId) {
        Ticket ticket = ticketsRepositoryPort.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("ticket not found"));
        ticket.setStatus(TicketStatus.CANCELLED);

        Event eventToCancel = eventsINPort.getById(ticket.getEventId());
        eventToCancel.setAvailableSeats(eventToCancel.getAvailableSeats() + 1);
        eventsINPort.update(eventToCancel);


        String body = "You have cancelled your ticket for eventId: "
                + eventToCancel.getId() +
                "If it is a mistake, please contact us";
        String subject = "Your ticked has been cancelled";

        notificationPort.sendNotification(new NotificationPort.NotificationDetails(ticket.getOwnerEmail(), subject, body));

        ticketsRepositoryPort.save(ticket);
    }


    @Override
    public List<MyTicketView> myTickets(String email) {
        System.out.println("service:  " + ticketsRepositoryPort.myTickets(email));
        return ticketsRepositoryPort.myTickets(email);
    }
}


