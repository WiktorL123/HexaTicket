package com.pjatk.web.controller;

import com.pjatk.core.domain.Event;
import com.pjatk.core.port.in.EventsPort;
import com.pjatk.web.dto.CreateEventDto;
import com.pjatk.web.dto.ResponseEventDto;
import com.pjatk.web.dto.UpdateEventDto;
import com.pjatk.web.mapper.DomainDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {
    private final EventsPort port;
    private final DomainDtoMapper mapper;

    @PostMapping
    public ResponseEntity<ResponseEventDto> create(@Valid @RequestBody CreateEventDto dto) {
        Event response = port.create(mapper.toEventDomain(dto));
        URI location = URI.create("events/" + response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(mapper.toEventDto(response));
    }
    @GetMapping
    public ResponseEntity<List<ResponseEventDto>> getAll(){
        List<Event> allEvents = port.findAll();
        List<ResponseEventDto> response = allEvents
                .stream()
                .map(event->mapper.toEventDto(event))
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){
        port.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEventDto> getById(@PathVariable("id") String id){
        Event found = port.getById(id);
        ResponseEventDto response = mapper.toEventDto(found);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseEventDto> updatePartially( @Valid @PathVariable("id") String id, @RequestBody UpdateEventDto dto){
        Event found = port.updatePartially(id, mapper.updateToEventDomain(dto));
        ResponseEventDto response = mapper.toEventDto(found);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
