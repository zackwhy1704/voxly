package app.voxly.repository;

import app.voxly.model.entity.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonEntity, UUID> {
    List<LessonEntity> findAllByOrderByUnitIdAscPositionAsc();
    List<LessonEntity> findByUnitIdOrderByPositionAsc(String unitId);
}
