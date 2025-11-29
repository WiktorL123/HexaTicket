package com.pjatk.mongo.mapper;

import com.pjatk.core.domain.Event;
import com.pjatk.mongo.model.EventDocument;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DomainMongoMapper {
   public Event toEventDomain(EventDocument document){

       return new Event(
                document.getId(),
                document.getName(),
                document.getDescription(),
                document.getVenue(),
                document.getStartDate(),
                document.getTotalSeats(),
                document.getAvailableSeats(),
                document.getPrice(),
                document.getCategory(),
                document.getStatus());
    }
    public EventDocument toEventDocument(Event event){
        return new EventDocument(
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
}
