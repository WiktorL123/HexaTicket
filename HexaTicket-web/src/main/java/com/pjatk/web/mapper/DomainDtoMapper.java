package com.pjatk.web.mapper;

import com.pjatk.core.domain.Event;
import com.pjatk.web.dto.CreateEventDto;
import com.pjatk.web.dto.ResponseEventDto;
import com.pjatk.web.dto.UpdateEventDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DomainDtoMapper {
    @NonNull
    public Event toEventDomain(CreateEventDto dto){
        return new Event(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getVenue(),
                dto.getStartDate(),
                dto.getTotalSeats(),
                dto.getAvailableSeats(),
                dto.getPrice(),
                dto.getCategory(),
                null

        );
    }
    @NonNull
    public ResponseEventDto toEventDto(Event event){
        return new ResponseEventDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getVenue(),
                event.getStartDate(),
                event.getTotalSeats(),
                event.getAvailableSeats(),
                event.getPrice(),
                event.getCategory(),
                event.getStatus()
        );
    }
    public Event updateToEventDomain(UpdateEventDto dto){
        return new Event(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getVenue(),
                dto.getStartDate(),
                dto.getTotalSeats(),
                dto.getAvailableSeats(),
                dto.getPrice(),
                dto.getCategory(),
                dto.getStatus()
        );
    }
}
