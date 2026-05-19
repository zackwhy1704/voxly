package app.voxly.service;

import app.voxly.exception.ResourceNotFoundException;
import app.voxly.model.dto.response.ChallengeResponse;
import app.voxly.model.dto.response.LessonResponse;
import app.voxly.model.entity.ChallengeEntity;
import app.voxly.model.entity.LessonEntity;
import app.voxly.model.entity.UserEntity;
import app.voxly.model.enums.NodeState;
import app.voxly.repository.ChallengeRepository;
import app.voxly.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningService {

    private final LessonRepository lessonRepository;
    private final ChallengeRepository challengeRepository;
    private final UserService userService;

    public List<LessonResponse> getPath(UUID userId) {
        return lessonRepository.findAllByOrderByUnitIdAscPositionAsc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public LessonResponse completeLesson(UUID lessonId, UUID userId) {
        LessonEntity lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + lessonId));
        lesson.setState(NodeState.COMPLETED);
        lesson = lessonRepository.save(lesson);

        final int completedPosition = lesson.getPosition();
        List<LessonEntity> unitLessons = lessonRepository.findByUnitIdOrderByPositionAsc(lesson.getUnitId());
        unitLessons.stream()
                .filter(l -> l.getPosition() == completedPosition + 1)
                .filter(l -> l.getState() == NodeState.LOCKED)
                .findFirst()
                .ifPresent(next -> {
                    next.setState(NodeState.IN_PROGRESS);
                    lessonRepository.save(next);
                });

        userService.addXp(userId, lesson.getXpReward());
        return toResponse(lesson);
    }

    public List<ChallengeResponse> getDailyChallenges(UUID userId) {
        UserEntity user = userService.findById(userId);
        return challengeRepository.findByResetsAtAfter(LocalDateTime.now())
                .stream()
                .map(c -> toChallengeResponse(c, c.getCompletedBy().contains(user)))
                .collect(Collectors.toList());
    }

    @Transactional
    public ChallengeResponse completeChallenge(UUID challengeId, UUID userId) {
        ChallengeEntity challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found: " + challengeId));
        UserEntity user = userService.findById(userId);
        challenge.getCompletedBy().add(user);
        challenge = challengeRepository.save(challenge);
        userService.addXp(userId, challenge.getXpReward());
        return toChallengeResponse(challenge, true);
    }

    private LessonResponse toResponse(LessonEntity e) {
        return LessonResponse.builder()
                .id(e.getId())
                .unitId(e.getUnitId())
                .position(e.getPosition())
                .title(e.getTitle())
                .type(e.getType())
                .state(e.getState())
                .xpReward(e.getXpReward())
                .phraseContent(e.getPhraseContent())
                .build();
    }

    private ChallengeResponse toChallengeResponse(ChallengeEntity e, boolean completed) {
        return ChallengeResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .xpReward(e.getXpReward())
                .accentColorHex(e.getAccentColorHex())
                .resetsAt(e.getResetsAt())
                .completed(completed)
                .build();
    }
}
