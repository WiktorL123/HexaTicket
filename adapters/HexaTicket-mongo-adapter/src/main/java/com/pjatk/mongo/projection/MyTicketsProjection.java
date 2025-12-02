package com.pjatk.mongo.projection;
import com.pjatk.mongo.model.EventDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyTicketsProjection {

    @Id
    @Field("_id")
    private String id;


    @Field("ownerName")
    private String ownerName;

    @Field("email")
    private String email;

    @Field("ticketCode")
    private String ticketCode;

    private EventDocument eventDetails;
}