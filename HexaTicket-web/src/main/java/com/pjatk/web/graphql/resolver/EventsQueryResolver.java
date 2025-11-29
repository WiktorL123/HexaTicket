package com.pjatk.web.graphql.resolver;

import com.pjatk.core.port.in.EventsPort;
import com.pjatk.web.graphql.contract.event.Event;
import com.pjatk.web.graphql.mapper.GraphqlDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventsQueryResolver {
   private final EventsPort port;
   private final GraphqlDomainMapper mapper;
    // W com/pjatk/web/graphql/resolver/EventsQueryResolver.java

    @QueryMapping
    public List<com.pjatk.web.graphql.contract.event.Event> events(
            @Argument("page") Integer page, // Zmienione na Integer
            @Argument("size") Integer size, // Zmienione na Integer
            @Argument("category") String category,
            @Argument("startDate") String startDate // Nadal String
    ){
        // 1. Obsługa domyślnych wartości (bo są Integer)
        int efectivePage = page != null ? page : 0;
        int effectiveSize = size != null ? size : 10;

        // 2. Obsługa i konwersja daty (Zabezpieczenie przed NullPointerException)
        LocalDateTime startDateTime = null;

        // Sprawdzamy, czy string daty w ogóle istnieje
        if (startDate != null) {
            try {
                startDateTime = LocalDateTime.parse(startDate);
            } catch (java.time.format.DateTimeParseException e) {
                // Używamy zaimplementowanego DataFetcherExceptionResolver
                throw new IllegalArgumentException("Nieprawidłowy format daty: użyj formatu ISO 8601 (YYYY-MM-DDTHH:MM:SS)");
            }
        }

        // 3. Delegacja
        var events = port.findAll(efectivePage, effectiveSize, category, startDateTime);

        return events.stream()
                .map(e->mapper.EventFromDomain(e))
                .toList();
    }
}
