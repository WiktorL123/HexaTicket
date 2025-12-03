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
import com.pjatk.core.port.out.NotificationPort;
import com.pjatk.core.port.out.TicketsRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock private TicketsRepositoryPort ticketsRepositoryPort;
    @Mock private EventsPort eventsINPort;
    @Mock private NotificationPort notificationPort;

    @InjectMocks private TicketService ticketService;

    private final String EVENT_ID = "event-123";
    ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
    private final String USER_EMAIL = "test@user.com";
    private BookTicketCommand command;
    private Event availableEvent;
    private Ticket savedTicket;
    private final String emailBody = "test body";
    private final String subject = "test subject";
    private final String getterEmail = "";

    @BeforeEach
    void setUp() {
        command = new BookTicketCommand(USER_EMAIL, "Jan Kowalski", EVENT_ID );

        availableEvent = new Event();
        availableEvent.setId(EVENT_ID);
        availableEvent.setName("Dostępny Koncert");
        availableEvent.setAvailableSeats(5);
        availableEvent.setEventStatus(EventStatus.ACTIVE);

        savedTicket = new Ticket();
        savedTicket.setId(UUID.randomUUID().toString());
        savedTicket.setTicketCode("ABCDEFGH");
        savedTicket.setOwnerEmail(USER_EMAIL);
        savedTicket.setStatus(TicketStatus.SOLD);
    }


    @Test
    void shouldBookTicketDecrementSeatsAndSendNotification() {
        when(eventsINPort.getById(EVENT_ID)).thenReturn(availableEvent);
        when(ticketsRepositoryPort.save(any(Ticket.class))).thenReturn(savedTicket);

        Ticket result = ticketService.bookTicket(command);

        verify(eventsINPort, times(1)).update(eventCaptor.capture());
        Event capturedEvent = eventCaptor.getValue();
        assertEquals(4, capturedEvent.getAvailableSeats(), "Dostepne miejsca powiiny byc zmniejszone o 1");

        ArgumentCaptor<NotificationPort.NotificationDetails> emailCaptor = ArgumentCaptor.forClass(NotificationPort.NotificationDetails.class);
        verify(notificationPort, times(1)).sendNotification(emailCaptor.capture());

        assertEquals(USER_EMAIL, emailCaptor.getValue().email());

        verify(ticketsRepositoryPort, times(1)).save(any(Ticket.class));
        assertEquals(TicketStatus.SOLD, result.getStatus());
    }


    @Test
    void shouldThrowTooMuchSeatsExceptionWhenNoSeatsAvailable() {
        availableEvent.setAvailableSeats(0);
        when(eventsINPort.getById(EVENT_ID)).thenReturn(availableEvent);

        assertThrows(TooMuchSeatsException.class, () -> {
            ticketService.bookTicket(command);
        }, "Powinien rzucić TooMuchSeatsException.");


        verify(ticketsRepositoryPort, never()).save(any(Ticket.class));
        verify(notificationPort, never()).sendNotification(new NotificationPort.NotificationDetails(getterEmail, subject, emailBody));
    }

    @Test
    void shouldThrowTicketCannotBeBookedExceptionWhenEventIsSoldOut() {
        availableEvent.setEventStatus(EventStatus.SOLD_OUT);
        when(eventsINPort.getById(EVENT_ID)).thenReturn(availableEvent);

        assertThrows(TicketCannotBeBookedException.class, () -> {
            ticketService.bookTicket(command);
        }, "Powinien rzucić TicketCannotBeBookedException.");
    }

    @Test
    void shouldThrowNotFoundExceptionIfEventDoesNotExist() {
        when(eventsINPort.getById(EVENT_ID)).thenThrow(new NotFoundException("Event not found"));

        assertThrows(NotFoundException.class, () -> {
            ticketService.bookTicket(command);
        });
    }


    @Test
    void shouldCancelTicketAndReturnSeatToEvent() {
        String ticketId = "ticket-to-cancel";
        Event eventBeforeCancel = availableEvent;
        Ticket ticketToCancel = new Ticket();
        ticketToCancel.setEventId(EVENT_ID);
        ticketToCancel.setOwnerEmail(USER_EMAIL);

        when(ticketsRepositoryPort.findById(ticketId)).thenReturn(Optional.of(ticketToCancel));
        when(eventsINPort.getById(EVENT_ID)).thenReturn(eventBeforeCancel);

        ticketService.cancelTicket(ticketId);

        assertEquals(6, eventBeforeCancel.getAvailableSeats(), "Dostępne miejsca powinny być ZWIĘKSZONE o 1 (5 -> 6).");
        assertEquals(TicketStatus.CANCELLED, ticketToCancel.getStatus(), "Status biletu powinien być CANCELLED.");

        verify(eventsINPort, times(1)).update(eventBeforeCancel);

        verify(ticketsRepositoryPort, times(1)).save(ticketToCancel);

        ArgumentCaptor<NotificationPort.NotificationDetails> emailCaptor = ArgumentCaptor.forClass(NotificationPort.NotificationDetails.class);
        verify(notificationPort, times(1)).sendNotification(emailCaptor.capture());

        assertEquals(USER_EMAIL, emailCaptor.getValue().email());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCancellingNonExistingTicket() {
        String nonExistingId = "non-existing";
        when(ticketsRepositoryPort.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            ticketService.cancelTicket(nonExistingId);
        });

        verify(eventsINPort, never()).create(any());
    }
}