package com.pjatk.web.graphql.contract.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketInput {
    String eventId;
    String ownerName;
    String ownerEmail;
}
