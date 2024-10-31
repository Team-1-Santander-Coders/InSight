package getterson.insight.entities;

import getterson.insight.entities.types.PreferenceType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topic_preferences")
public class TopicPreferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
}
