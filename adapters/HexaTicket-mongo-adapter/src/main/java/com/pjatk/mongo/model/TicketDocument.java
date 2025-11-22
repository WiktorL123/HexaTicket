package com.pjatk.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(collection = "tickets")
public class TicketDocument {

    @Id
    private String id;
    @DocumentReference
    private EventDocument event;

    @Field(name = "ticket_code")
    private UUID ticketCode;

    @Field(name = "owner_email")
    private String ownerEmail;

    @Field(name = "owner_name")
    private String ownerName;

    @Field(name = "reserved_at")
    private LocalDate reservedAt;

    private String status;
}
