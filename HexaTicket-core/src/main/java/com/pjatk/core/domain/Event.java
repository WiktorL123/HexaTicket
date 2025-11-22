package com.pjatk.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String id;
    private String name;
    private String description;
    private String venue;
    private LocalDateTime startDate;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal price;
    private String category;
    private String status;
}
