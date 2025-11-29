package com.pjatk.web.graphql.mapper;

import com.pjatk.core.domain.Event;
import com.pjatk.web.graphql.contract.event.CreateEventInput;
import com.pjatk.web.graphql.contract.event.UpdateEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class GraphqlDomainMapper {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public Event createInputToEventDomain(CreateEventInput input){
        LocalDateTime startDate = LocalDateTime.parse(input.startDate());
        return new Event(
                null,
                input.name(),
                input.description(),
                input.venue(),
                startDate,
                input.totalSeats(),
                null,
                input.price(),
                input.category(),
                null

        );
    }


    public Event updateToEventDomain(UpdateEvent input) {
        LocalDateTime startDate = input.startDate() != null ? LocalDateTime.parse(input.startDate()) : null;
        return new Event(
                null,
                input.name(),
                input.description(),
                input.venue(),
                startDate,
                input.totalSeats(),
                input.availableSeats(),
                input.price(),
                input.category(),
                input.status()
        );
    }
    public com.pjatk.web.graphql.contract.event.Event EventFromDomain(Event event){
            String startDate = event.getStartDate() != null ? event.getStartDate().format(ISO_FORMATTER) :
                    null;
        return com.pjatk.web.graphql.contract.event.Event.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .startDate(startDate)
                .venue(event.getVenue())
                .availableSeats(event.getAvailableSeats())
                .totalSeats(event.getTotalSeats())
                .price(event.getPrice())
                .category(event.getCategory())
                .status(event.getStatus())
                .build();
    }
}
