package com.pjatk.web.rest.dto;

import com.pjatk.core.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor

public class ResponseEventDto {
    private String id;
    private String name;
    private String description;
    private String venue;
    private LocalDateTime startDate;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal price;
    private String category;
    private Status status;
}
