package com.pjatk.mongo.model;

import com.pjatk.core.domain.event.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "events")
public class EventDocument {
    @Id
    private String id;
    private String name;
    private String description;
    private String venue;

    @Field(name = "start_date")
    private LocalDateTime startDate;

    @Field(name = "total_seats")
    private Integer totalSeats;
    @Field(name = "available_seats")
    private Integer availableSeats;

    private BigDecimal price;
    @Indexed
    private String category;
    private EventStatus eventStatus;
}
