package getterson.insight.repositories;

import getterson.insight.dtos.TopicPreferenceDTO;
import getterson.insight.entities.TopicPreferenceEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicPreferenceRepository extends JpaRepository<TopicPreferenceEntity, Long> {

    @Query("SELECT new getterson.insight.dtos.TopicPreferenceDTO(t.id, t.user.id, t.sendNewsLetter, t.type) "
            + "FROM TopicPreferenceEntity t "
            + "WHERE t.user.id = :userId")
    List<TopicPreferenceDTO> findAllByUserIdAsDTO(
            @Param("userId") long userId
    );
}