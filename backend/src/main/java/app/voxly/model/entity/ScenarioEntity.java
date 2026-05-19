package app.voxly.model.entity;

import app.voxly.model.enums.DifficultyTier;
import app.voxly.model.enums.ScenarioCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "scenarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Enumerated(EnumType.STRING)
    private DifficultyTier tier;

    @Enumerated(EnumType.STRING)
    private ScenarioCategory category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private int durationMinutes = 15;

    private long accentColorHex;

    @Builder.Default
    private int xpReward = 30;

    @Builder.Default
    private boolean isLocked = false;

    @ElementCollection
    @CollectionTable(name = "scenario_skills", joinColumns = @JoinColumn(name = "scenario_entity_id"))
    @Column(name = "skills_tested")
    @Builder.Default
    private List<String> skillsTested = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}
