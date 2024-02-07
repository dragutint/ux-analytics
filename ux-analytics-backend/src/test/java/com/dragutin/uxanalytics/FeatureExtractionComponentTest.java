package com.dragutin.uxanalytics;

import com.dragutin.uxanalytics.config.AbstractComponentTest;
import com.dragutin.uxanalytics.dto.features.FeatureDto;
import com.dragutin.uxanalytics.entity.UserJourneyEntity;
import com.dragutin.uxanalytics.repository.UserJourneyEntityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class FeatureExtractionComponentTest extends AbstractComponentTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserJourneyEntityRepository repository;

    @Value("classpath:user-journey.json")
    private Resource json;

    @BeforeEach
    public void truncate() {

        repository.deleteAll();
    }

    @Test
    void test() throws Exception {

        // load user journey from resources
        String contentAsString = json.getContentAsString(StandardCharsets.UTF_8);

        final UserJourneyEntity userJourney = objectMapper.readValue(
                contentAsString,
                UserJourneyEntity.class
        );

        // save user journey
        repository.save(userJourney);

        // create
        final MvcResult postResult = mockMvc.perform(
                post("/user-journeys/" + userJourney.getEmail() + "/quantify")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();

        // assert result after create
        assertEquals(200, postResult.getResponse().getStatus());

        // get user journey
        final UserJourneyEntity userJourneyEntity = repository.findById(userJourney.getEmail()).get();

        // assert user journey
        final List<FeatureDto> features = userJourneyEntity.getFeatures();

        assertNotNull(features);

        assertFeature("totalMouseLength", "6663.007336806887px", features);
        assertFeature("totalClicks", "4", features);
        assertFeature("totalScrollLength", "2814px", features);
        assertFeature("totalKeyboardPresses", "28", features);
        assertFeature("numberOfScrollDirectionChanges", "2", features);

    }

    private void assertFeature(String featureName, String featureValue, List<FeatureDto> features) {

        final FeatureDto feature = features.stream()
                .filter(f -> f.getName().equals(featureName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Feature does not exist: " + featureName));

        assertEquals(featureValue, feature.getValue());
    }
}