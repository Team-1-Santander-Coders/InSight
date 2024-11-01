package getterson.insight.repositories;

import getterson.insight.entities.UserPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreferenceEntity, Long> {
}
