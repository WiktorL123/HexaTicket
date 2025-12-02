package com.pjatk.web.graphql.contract.ticket;

import com.pjatk.web.graphql.contract.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyTicketView {
    String ownerName;
    String ownerEmail;
    Event event;
}
