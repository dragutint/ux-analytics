package com.dragutin.uxanalytics.repository;

import com.dragutin.uxanalytics.dto.actions.KeyboardActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import com.dragutin.uxanalytics.entity.UserJourneyEntity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class UserJourneyEntityRepositoryCustomImpl implements UserJourneyEntityRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void appendEvents(String email, List<MouseActionDto> mouseActions, List<KeyboardActionDto> keyboardActions) {

        Update update = new Update();

        if(mouseActions != null && !mouseActions.isEmpty()) {
            update.push("mouseActions").each(mouseActions);
        }

        if(keyboardActions != null && !keyboardActions.isEmpty()) {
            update.push("keyboardActions").each(keyboardActions);
        }

        mongoTemplate.updateFirst(
                query(where("email").is(email)),
                update,
                UserJourneyEntity.class
        );
    }
}
