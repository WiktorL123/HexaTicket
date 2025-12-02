package com.pjatk.web.graphql.resolver;

import com.pjatk.core.command.BookTicketCommand;
import com.pjatk.core.port.in.TicketsPort;
import com.pjatk.web.graphql.contract.ticket.BookTicketInput;
import com.pjatk.web.graphql.mapper.GraphqlDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import com.pjatk.web.graphql.contract.ticket.Ticket;

@Controller
@RequiredArgsConstructor
public class TicketsMutationResolver {
    private final TicketsPort port;
    private final GraphqlDomainMapper mapper;
    @MutationMapping
    public Ticket bookEvent(@Argument("input")BookTicketInput input){

        BookTicketCommand command = mapper.inputToCommand(input);
        com.pjatk.core.domain.ticket.Ticket  resultDomain = port.bookTicket(command);
        return mapper.ticketFromDomain(resultDomain);
    }
    @MutationMapping
    public String cancelTicket(@Argument("ticketId") String id){
        port.cancelTicket(id);
        return  "Succesfully cancelled Ticket";
    }
}
