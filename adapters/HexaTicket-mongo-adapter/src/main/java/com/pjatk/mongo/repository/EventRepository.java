package com.pjatk.mongo.repository;

import com.pjatk.mongo.model.EventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<EventDocument, String> {
}

