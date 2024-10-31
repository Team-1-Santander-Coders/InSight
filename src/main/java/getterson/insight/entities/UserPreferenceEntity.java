package getterson.insight.entities;

import getterson.insight.entities.types.PreferenceType;
import jakarta.persistence.*;
import lombok.*;

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
    private long id;

    @ElementCollection
    private List<String> blackListWords;

    @OneToOne
    @JoinColumn(name = "user_id")
    @Column(nullable = false, unique = true)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreferenceType type;

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
}
