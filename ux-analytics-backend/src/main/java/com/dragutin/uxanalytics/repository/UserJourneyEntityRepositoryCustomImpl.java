package com.dragutin.uxanalytics.repository;

import com.dragutin.uxanalytics.dto.requests.UserJourneyEventsRequest;
import com.dragutin.uxanalytics.entity.UserJourneyEntity;
import com.dragutin.uxanalytics.entity.UserJourneyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class UserJourneyEntityRepositoryCustomImpl implements UserJourneyEntityRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void appendEvents(String token, UserJourneyEventsRequest request) {

        Update update = new Update();

        update.set("status", UserJourneyStatus.IN_PROGRESS);
        update.set("lastEventAt", Instant.now());

        if(request.getMouseActions() != null && !request.getMouseActions().isEmpty()) {
            update.push("mouseActions").each(request.getMouseActions());
        }

        if(request.getKeyboardActions() != null && !request.getKeyboardActions().isEmpty()) {
            update.push("keyboardActions").each(request.getKeyboardActions());
        }

        if(request.getScrollActions() != null && !request.getScrollActions().isEmpty()) {
            update.push("scrollActions").each(request.getScrollActions());
        }

        mongoTemplate.updateFirst(
                query(where("token").is(token)),
                update,
                UserJourneyEntity.class
        );
    }

    @Override
    public List<UserJourneyEntity> findStaleUserJourneys(int thresholdInMinutes) {

        return mongoTemplate.find(
                query(where("status").is(UserJourneyStatus.IN_PROGRESS)
                        .and("lastEventAt").lt(Instant.now().minusSeconds(thresholdInMinutes * 60L))),
                UserJourneyEntity.class
        );
    }
}
