package com.dragutin.uxanalytics.entity.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_journey", uniqueConstraints = @UniqueConstraint(columnNames = {"token"}))
public class UserJourneyJpaEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String token;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Integer formNumber;

    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_journey_id", referencedColumnName = "id")
    private List<UserJourneyCharacteristicJpaEntity> characteristics;

}
