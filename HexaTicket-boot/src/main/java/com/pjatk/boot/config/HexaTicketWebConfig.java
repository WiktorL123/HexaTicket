package com.pjatk.boot.config;

import com.pjatk.application.service.EventService;
import com.pjatk.application.service.TicketService;
import com.pjatk.core.port.in.EventsPort;
import com.pjatk.core.port.in.TicketsPort;
import com.pjatk.core.port.out.EventsRepositoryPort;
import com.pjatk.core.port.out.TicketsRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HexaTicketWebConfig {
    @Bean
    TicketsPort ticketsPort(TicketsRepositoryPort port){
        return new TicketService(port);
    }
    @Bean
    EventsPort eventsPort(EventsRepositoryPort port){
        return new EventService(port);
    }
}
