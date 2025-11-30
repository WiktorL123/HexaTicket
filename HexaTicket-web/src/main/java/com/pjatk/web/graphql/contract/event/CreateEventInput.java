package com.pjatk.web.graphql.contract.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateEventInput(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        String venue,
        @NotNull
        String startDate,
        @NotNull
        Integer totalSeats,
        @NotNull
        BigDecimal price,
        @NotNull
        String category
) {
}
