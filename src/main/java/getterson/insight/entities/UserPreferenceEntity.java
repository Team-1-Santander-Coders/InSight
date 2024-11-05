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

    @ElementCollection
    private List<String> blackListWords;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreferenceType type;

    public UserPreferenceEntity(UserEntity user) {
        this.blackListWords = new ArrayList<String>();
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

    public UserPreferenceEntity(List<String> blackListWords, UserEntity user, PreferenceType type){
        this.blackListWords = blackListWords;
        this.user = user;
        this.type = type;
    }
}
