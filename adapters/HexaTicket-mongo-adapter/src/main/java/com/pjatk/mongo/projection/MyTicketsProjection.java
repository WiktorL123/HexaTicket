package com.pjatk.mongo.projection;

import com.pjatk.mongo.model.EventDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyTicketsProjection {
    String ownerName;
    String email;
    List<EventDocument> eventDetails;
}
