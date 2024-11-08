package getterson.insight.entities;

import getterson.insight.entities.types.PreferenceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topic_preferences")
public class TopicPreferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean sendNewsLetter;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "topic_id")
    private TopicEntity topicEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreferenceType type;

    public TopicPreferenceEntity(UserEntity user, TopicEntity topic) {
        this.user = user;
        this.sendNewsLetter = false;
        this.topicEntity = topic;
        this.type = PreferenceType.TOPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicPreferenceEntity that)) return false;
        return Objects.equals(user, that.user) && Objects.equals(topicEntity, that.topicEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, topicEntity);
    }
}
