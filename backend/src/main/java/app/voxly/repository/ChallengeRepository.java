package app.voxly.repository;

import app.voxly.model.entity.ChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, UUID> {
    List<ChallengeEntity> findByResetsAtAfter(LocalDateTime now);
}
