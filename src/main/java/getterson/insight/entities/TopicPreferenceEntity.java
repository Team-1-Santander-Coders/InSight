package getterson.insight.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topic_preferences")
public class TopicPreferenceEntity extends PreferenceEntity {
    private boolean sendNewsLetter;

    @OneToOne
    @JoinColumn(name = "topic_id")
    private TopicEntity topicEntity;
}
