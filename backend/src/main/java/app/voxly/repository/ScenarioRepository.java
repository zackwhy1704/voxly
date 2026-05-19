package app.voxly.repository;

import app.voxly.model.entity.ScenarioEntity;
import app.voxly.model.enums.DifficultyTier;
import app.voxly.model.enums.ScenarioCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ScenarioRepository extends JpaRepository<ScenarioEntity, UUID> {
    List<ScenarioEntity> findByTier(DifficultyTier tier);
    List<ScenarioEntity> findByCategory(ScenarioCategory category);
    List<ScenarioEntity> findByTierAndCategory(DifficultyTier tier, ScenarioCategory category);
    List<ScenarioEntity> findByIsLockedFalse();
}
