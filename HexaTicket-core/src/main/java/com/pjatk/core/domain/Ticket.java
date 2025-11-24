package com.pjatk.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String id;

    private String eventId;

    private UUID ticketCode;

    private String ownerEmail;

    private String ownerName;

    private LocalDate reservedAt;

    private String status;
}
