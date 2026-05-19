package app.voxly.repository;

import app.voxly.model.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {
    List<SessionEntity> findByUserIdOrderByCompletedAtDesc(UUID userId, Pageable pageable);
    List<SessionEntity> findByUserIdAndCompletedAtIsNotNullOrderByCompletedAtDesc(UUID userId, Pageable pageable);
}
