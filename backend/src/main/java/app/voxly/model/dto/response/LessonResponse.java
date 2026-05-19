package app.voxly.model.dto.response;

import app.voxly.model.enums.LessonType;
import app.voxly.model.enums.NodeState;
import lombok.*;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LessonResponse {
    private UUID id;
    private String unitId;
    private int position;
    private String title;
    private LessonType type;
    private NodeState state;
    private int xpReward;
    private String phraseContent;
}
