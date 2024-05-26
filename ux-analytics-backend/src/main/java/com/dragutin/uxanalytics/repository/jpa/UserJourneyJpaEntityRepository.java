package com.dragutin.uxanalytics.repository.jpa;

import com.dragutin.uxanalytics.entity.jpa.UserJourneyJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserJourneyJpaEntityRepository extends JpaRepository<UserJourneyJpaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserJourneyJpaEntity u WHERE u.token = :token")
    UserJourneyJpaEntity lockByToken(@Param("token") String token);
}
