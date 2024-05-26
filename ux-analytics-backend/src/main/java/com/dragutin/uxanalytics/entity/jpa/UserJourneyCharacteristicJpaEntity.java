package com.dragutin.uxanalytics.entity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_journey_characteristic")
public class UserJourneyCharacteristicJpaEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    private String unit;

    private String value;

}
