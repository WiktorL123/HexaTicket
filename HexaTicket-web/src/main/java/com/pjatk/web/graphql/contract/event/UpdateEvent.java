package com.pjatk.web.graphql.contract.event;

import com.pjatk.core.domain.event.EventStatus;

import java.math.BigDecimal;

public record UpdateEvent(
        String name,
        String description,
        String startDate,
        String venue,
        Integer totalSeats,
        Integer availableSeats,
        BigDecimal price,
        String category,
        EventStatus eventStatus) {}
