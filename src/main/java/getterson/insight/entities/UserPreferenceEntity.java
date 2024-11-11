package getterson.insight.entities;

import getterson.insight.entities.types.PreferenceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_preferences")
public class UserPreferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean sendNotificationWhenReady;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreferenceType type;

    public UserPreferenceEntity(UserEntity user) {
        this.sendNotificationWhenReady = true;
        this.user = user;
        this.type = PreferenceType.USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPreferenceEntity that)) return false;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }

    public UserPreferenceEntity(boolean sendNotificationWhenReady, UserEntity user, PreferenceType type){
        this.sendNotificationWhenReady = sendNotificationWhenReady;
        this.user = user;
        this.type = type;
    }

}
