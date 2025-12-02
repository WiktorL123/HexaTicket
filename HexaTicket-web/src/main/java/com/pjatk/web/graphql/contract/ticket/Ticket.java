package com.pjatk.web.graphql.contract.ticket;

import com.pjatk.core.domain.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    String id;
    String eventId;
    String ticketCode;

    String ownerName;

    String ownerEmail;
    TicketStatus status;
}
