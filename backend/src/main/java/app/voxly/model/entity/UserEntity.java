package app.voxly.model.entity;

import app.voxly.model.enums.DifficultyTier;
import app.voxly.model.enums.LearningGoal;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String country;

    @Enumerated(EnumType.STRING)
    private LearningGoal goal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DifficultyTier currentTier = DifficultyTier.BRONZE;

    @Column(nullable = false)
    @Builder.Default
    private int careerReadinessScore = 0;

    @Column(nullable = false)
    @Builder.Default
    private int streakDays = 0;

    @Column(nullable = false)
    @Builder.Default
    private int totalXp = 0;

    @Column(nullable = false)
    @Builder.Default
    private int weeklyXp = 0;

    @Column(nullable = false)
    @Builder.Default
    private String rank = "Bronze I";

    @Column(nullable = false)
    @Builder.Default
    private boolean emailVerified = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
