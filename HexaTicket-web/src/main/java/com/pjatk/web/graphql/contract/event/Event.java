package com.pjatk.web.graphql.contract.event;

import com.pjatk.core.domain.event.EventStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Event(
        String id,
        String name,
        String description,
        String startDate,
        String venue,
        Integer availableSeats,
        Integer totalSeats,
        BigDecimal price,
        String category,
        EventStatus eventStatus
) {
}
