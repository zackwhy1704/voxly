package app.voxly.model.entity;

import app.voxly.model.enums.LessonType;
import app.voxly.model.enums.NodeState;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "lessons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String unitId;
    private int position;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private LessonType type;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private NodeState state = NodeState.LOCKED;

    @Builder.Default
    private int xpReward = 15;

    @Column(columnDefinition = "TEXT")
    private String phraseContent;

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String pronunciationTipsJson = "[]";
}
