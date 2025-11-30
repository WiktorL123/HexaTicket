package com.pjatk.web.graphql.resolver;

import com.pjatk.core.domain.Event;
import com.pjatk.core.port.in.EventsPort;
import com.pjatk.web.graphql.contract.event.CreateEventInput;
import com.pjatk.web.graphql.contract.event.UpdateEvent;
import com.pjatk.web.graphql.mapper.GraphqlDomainMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EventsMutationResolver {
    private final EventsPort port;
    private final GraphqlDomainMapper mapper;
    @MutationMapping
    public com.pjatk.web.graphql.contract.event.Event createEvent(@Valid  @Argument("input") CreateEventInput input){
        System.out.println("[RESOLVER] creating new data: " + input);
        Event eventToCreate = mapper.createInputToEventDomain(input);
        Event created = port.create(eventToCreate);
        return mapper.EventFromDomain(created);

    }
    @MutationMapping
    public com.pjatk.web.graphql.contract.event.Event updateEvent(
            @Argument("id") String id,
            @Argument("input") UpdateEvent input

    ){
        System.out.println("[RESOLVER] Entering update. ID: " + id + ", Input: " + input); // Ten log MUSI się wykonać


        Event eventToUpdate = mapper.updateToEventDomain(input);
        Event updated = port.updatePartially(id, eventToUpdate);
        return mapper.EventFromDomain(updated);
    }

    @MutationMapping
    String deleteEvent(@Argument("id") String id) {
        port.delete(id);
        return id;
    }
}
