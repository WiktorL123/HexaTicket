package com.pjatk.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateEventDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String venue;
    private LocalDateTime startDate;
    @NotNull
    private Integer totalSeats;
    @NotNull
    private Integer availableSeats;
    @NotNull
    private BigDecimal price;
    @NotBlank
    private String category;
    @NotBlank
    private String status;
}
