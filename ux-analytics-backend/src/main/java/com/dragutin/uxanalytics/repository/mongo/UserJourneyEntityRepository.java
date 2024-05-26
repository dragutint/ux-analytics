package com.dragutin.uxanalytics.repository.mongo;

import com.dragutin.uxanalytics.entity.mongo.UserJourneyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJourneyEntityRepository extends MongoRepository<UserJourneyEntity, String>, UserJourneyEntityRepositoryCustom {

}
