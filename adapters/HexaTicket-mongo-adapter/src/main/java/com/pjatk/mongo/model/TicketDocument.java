package com.pjatk.mongo.model;

import com.pjatk.core.domain.ticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(collection = "tickets")
public class TicketDocument {

    @Id
    private String id;
    @Indexed
    private String eventId;

    @Field(name = "ticket_code")
    private String ticketCode;

    @Field(name = "owner_email")
    private String ownerEmail;

    @Field(name = "owner_name")
    private String ownerName;

    @Field(name = "reserved_at")
    private LocalDateTime reservedAt;

    private TicketStatus status;
}
