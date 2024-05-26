package com.dragutin.uxanalytics;

import com.dragutin.uxanalytics.config.AbstractComponentTest;
import com.dragutin.uxanalytics.dto.ScreenInfoDto;
import com.dragutin.uxanalytics.dto.UserDto;
import com.dragutin.uxanalytics.dto.actions.KeyboardActionDto;
import com.dragutin.uxanalytics.dto.actions.MouseActionDto;
import com.dragutin.uxanalytics.dto.requests.CreateUserJourneyRequest;
import com.dragutin.uxanalytics.dto.requests.UserJourneyEventsRequest;
import com.dragutin.uxanalytics.dto.responses.CreateUserJourneyResponse;
import com.dragutin.uxanalytics.entity.mongo.UserJourneyEntity;
import com.dragutin.uxanalytics.repository.mongo.UserJourneyEntityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class UserJourneyComponentTest extends AbstractComponentTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserJourneyEntityRepository repository;

    @BeforeEach
    public void truncate() {

        repository.deleteAll();
    }

    @Test
    void test() throws Exception {

        // create user journey request
        CreateUserJourneyRequest request = new CreateUserJourneyRequest();
        UserDto user = new UserDto();
        user.setEmail("dragutin.todorovicx@gmail.com");
        request.setUser(user);
        ScreenInfoDto screenInfo = new ScreenInfoDto();
        screenInfo.setPageLengthInPixels(1080);
        screenInfo.setPageWidthInPixels(1920);
        request.setScreenInfo(screenInfo);

        // convert to json
        String content = objectMapper.writeValueAsString(request);

        // create
        final MvcResult postResult = mockMvc.perform(
                post("/user-journeys")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();

        // assert result after create
        assertEquals(200, postResult.getResponse().getStatus());

        // deserialize response
        final CreateUserJourneyResponse response = objectMapper.readValue(postResult.getResponse().getContentAsString(), CreateUserJourneyResponse.class);

        // assert response
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals(request.getUser().getEmail(), response.getEmail());

        // create append events request
        UserJourneyEventsRequest appendRequest = new UserJourneyEventsRequest();
        final List<MouseActionDto> mouseActions = List.of(
                MouseActionDto.builder()
                        .action("mousemove")
                        .x(2L)
                        .y(4L)
                        .timestamp(LocalDateTime.now().minusSeconds(5))
                        .build(),
                MouseActionDto.builder()
                        .action("mousemove")
                        .x(3L)
                        .y(5L)
                        .timestamp(LocalDateTime.now().minusSeconds(4))
                        .build(),
                MouseActionDto.builder()
                        .action("mousemove")
                        .x(4L)
                        .y(6L)
                        .timestamp(LocalDateTime.now().minusSeconds(3))
                        .build(),
                MouseActionDto.builder()
                        .action("mousemove")
                        .x(5L)
                        .y(7L)
                        .timestamp(LocalDateTime.now().minusSeconds(2))
                        .build()
        );
        appendRequest.setMouseActions(mouseActions);
        final List<KeyboardActionDto> keyboardAction = List.of(
                KeyboardActionDto.builder()
                        .key("a")
                        .timestamp(LocalDateTime.now().minusSeconds(1))
                        .build(),
                KeyboardActionDto.builder()
                        .key("b")
                        .timestamp(LocalDateTime.now())
                        .build(),
                KeyboardActionDto.builder()
                        .key("c")
                        .timestamp(LocalDateTime.now().plusSeconds(1))
                        .build()
        );
        appendRequest.setKeyboardActions(keyboardAction);

        // convert to json
        String appendContent = objectMapper.writeValueAsString(appendRequest);

        for(int i = 0; i < 1000; i++) {
            // append
            final MvcResult appendResult = mockMvc.perform(
                    put("/user-journeys/dragutin.todorovicx@gmail.com")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(appendContent)
                            .accept(MediaType.APPLICATION_JSON)
            ).andReturn();

            // assert result after append
            assertEquals(200, appendResult.getResponse().getStatus());
        }

        // assert result after append
        final UserJourneyEntity userJourneyEntity = repository.findById("dragutin.todorovicx@gmail.com").get();

        assertEquals(4000, userJourneyEntity.getMouseActions().size());
        assertEquals(3000, userJourneyEntity.getKeyboardActions().size());
    }
}