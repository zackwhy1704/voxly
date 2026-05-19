package app.voxly.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private ScenarioEntity scenario;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @Builder.Default
    private int durationSeconds = 0;

    @Builder.Default
    private int overallScore = 0;
    @Builder.Default
    private int pronunciationScore = 0;
    @Builder.Default
    private int fluencyScore = 0;
    @Builder.Default
    private int grammarScore = 0;
    @Builder.Default
    private int vocabularyScore = 0;
    @Builder.Default
    private int xpEarned = 0;

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String aiInsightsJson = "[]";

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String flaggedWordsJson = "[]";

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String fillerWordsJson = "[]";

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String pacingDataJson = "[]";

    @CreationTimestamp
    private LocalDateTime createdAt;
}
