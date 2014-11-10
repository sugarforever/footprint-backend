package com.wiysoft.footprint.backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.wiysoft.footprint.backend.model.Footprint;

@Repository
public interface FootprintRepository extends MongoRepository<Footprint, String> {

}
