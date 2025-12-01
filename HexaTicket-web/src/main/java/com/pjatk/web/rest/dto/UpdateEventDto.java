package com.pjatk.web.rest.dto;

import com.pjatk.core.domain.event.EventStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {
    @Size(min = 3, max = 100, message = "Nazwa musi mieć od 3 do 100 znaków")
    private String name;

    @Size(max = 500, message = "Opis nie może być dłuższy niż 500 znaków")
    private String description;

    private String venue;

    @Future(message = "Data wydarzenia musi być w przyszłości")
    private LocalDateTime startDate;

    @Min(value = 1, message = "Musi być przynajmniej 1 miejsce")
    private Integer totalSeats;

    @Min(value = 0, message = "Liczba miejsc nie może być ujemna")
    private Integer availableSeats;

    @DecimalMin(value = "0.0", inclusive = true, message = "Cena nie może być ujemna")
    private BigDecimal price;

    private String category;

    private EventStatus eventStatus;
}
