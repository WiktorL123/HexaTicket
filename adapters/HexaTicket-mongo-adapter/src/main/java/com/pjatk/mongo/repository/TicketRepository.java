package com.pjatk.mongo.repository;

import com.pjatk.mongo.model.TicketDocument;
import com.pjatk.mongo.projection.MyTicketsProjection;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TicketRepository extends MongoRepository<TicketDocument, String> {
    @Aggregation(pipeline = {
            "{ '$addFields': { 'convertedEventId': { $toObjectId: '$eventId' } } }",

            // KROK 2: FILTROWANIE
            "{'$match': {'owner_email':  ?0}}",

            "{ '$lookup':  {'from': 'events', 'localField' :  'convertedEventId', 'foreignField':  '_id', 'as':  'eventDetails'}}",

            "{'$unwind':  '$eventDetails'}",

            "{'$project':  {" +
                    "'_id': '$_id'," +
                    "'ownerName': '$owner_name',"+
                    "'email': '$owner_email',"+
                    "'ticketCode': '$ticket_code',"+
                    "'eventDetails':  '$eventDetails',"+
                    "}}"
    })
    List<MyTicketsProjection> findMyTicketsByOwnerEmailAggregation(String email);
}