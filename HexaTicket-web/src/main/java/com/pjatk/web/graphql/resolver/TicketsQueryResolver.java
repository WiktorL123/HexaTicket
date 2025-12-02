package com.pjatk.web.graphql.resolver;

import com.pjatk.core.port.in.TicketsPort;
import com.pjatk.web.graphql.contract.ticket.MyTicketView;
import com.pjatk.web.graphql.mapper.GraphqlDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketsQueryResolver {
    private final TicketsPort port;
    private final GraphqlDomainMapper mapper;

    @QueryMapping
    public List<MyTicketView> myTickets(@Argument("email") String email) {
        List<com.pjatk.core.view.MyTicketView> eventsDomain = port.myTickets(email);
        return eventsDomain
                .stream()
                .map(e->mapper.toMyTicketViewDto(e))
                .toList();
    }
}
