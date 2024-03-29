package com.dragutin.uxanalytics.repository;

import com.dragutin.uxanalytics.entity.UserJourneyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJourneyEntityRepository extends MongoRepository<UserJourneyEntity, String>, UserJourneyEntityRepositoryCustom {

}
