package com.pjatk.web.controller;

import com.pjatk.core.domain.Event;
import com.pjatk.core.port.in.EventsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {
    private final EventsPort port;

    @PostMapping
    public Event create(Event event){
        return port.create(event);
    }
}
