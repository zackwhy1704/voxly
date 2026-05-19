package app.voxly.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "daily_challenges")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private int xpReward = 20;

    private long accentColorHex;
    private LocalDateTime resetsAt;

    @ManyToMany
    @JoinTable(
        name = "challenge_completions",
        joinColumns = @JoinColumn(name = "challenge_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<UserEntity> completedBy = new HashSet<>();
}
