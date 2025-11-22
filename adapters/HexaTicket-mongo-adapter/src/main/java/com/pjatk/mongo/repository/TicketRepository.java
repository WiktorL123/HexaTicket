package com.pjatk.mongo.repository;

import com.pjatk.mongo.model.TicketDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<TicketDocument, String> {}
