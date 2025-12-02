package com.pjatk.mongo.mapper;

import com.pjatk.core.domain.event.Event;
import com.pjatk.core.domain.ticket.Ticket;
import com.pjatk.core.view.MyTicketView;
import com.pjatk.mongo.model.EventDocument;
import com.pjatk.mongo.model.TicketDocument;
import com.pjatk.mongo.projection.MyTicketsProjection;
import org.springframework.stereotype.Component;

import java.util.List;

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
                document.getEventStatus());
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
                event.getEventStatus()
        );
    }
    public TicketDocument toTicketDocument(Ticket ticket){
       return new TicketDocument(
               ticket.getId(),
               ticket.getEventId(),
               ticket.getTicketCode(),
               ticket.getOwnerEmail(),
               ticket.getOwnerName(),
               ticket.getReservedAt(),
               ticket.getStatus()
       );
    }
    public Ticket toTicketDomain(TicketDocument document){
       return new Ticket(
               document.getId(),
               document.getEventId(),
               document.getTicketCode(),
               document.getOwnerEmail(),
               document.getOwnerName(),
               document.getReservedAt(),
               document.getStatus()
       );
    }
    public MyTicketView projectionToTicketView(MyTicketsProjection projection){
        List<Event> eventDomainDetails =  projection.getEventDetails()
                .stream()
                .map(doc->toEventDomain(doc))
                .toList();
        return new MyTicketView(
                projection.getOwnerName(),
                projection.getEmail(),
                eventDomainDetails.get(0)
        );
    }
}
