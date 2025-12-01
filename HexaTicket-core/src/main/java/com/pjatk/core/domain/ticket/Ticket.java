package com.pjatk.core.domain.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String id;

    private String eventId;

    private String ticketCode;

    private String ownerEmail;

    private String ownerName;

    private LocalDateTime reservedAt;

    private TicketStatus status;
}
