package com.pjatk.core.port.in;

import com.pjatk.core.command.BookTicketCommand;
import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.ticket.Ticket;
import com.pjatk.core.view.MyTicketView;


import java.util.List;

public interface TicketsPort {
    Ticket bookTicket(BookTicketCommand command);

    void cancelTicket(String ticketId);

    List<MyTicketView> myTickets(String email);
}
