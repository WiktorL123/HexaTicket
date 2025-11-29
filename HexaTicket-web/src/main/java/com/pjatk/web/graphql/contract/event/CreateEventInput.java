package com.pjatk.web.graphql.contract.event;

import java.math.BigDecimal;

public record CreateEventInput(
        String name,
        String description,
        String venue,
        String startDate,
        Integer totalSeats,
        BigDecimal price,
        String category
) {
}
