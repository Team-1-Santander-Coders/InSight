package getterson.insight.entities;

import getterson.insight.entities.types.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String document;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserPreferenceEntity userPreference;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TopicPreferenceEntity> topicPreferenceList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TopicEntity> topicList;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TokenEntity> tokens;

    public UserEntity(String name, String username, String document, LocalDate birthDate, String email, String password, UserType type) {
        this.name = name;
        this.username = username;
        this.document = document;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public void addTopicPreference(TopicPreferenceEntity topicPreference) {
        this.topicPreferenceList.add(topicPreference);
    }

    public void addTopic(TopicEntity topic) {
        this.topicList.add(topic);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return Objects.equals(username, that.username) || Objects.equals(document, that.document) || Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, document, email);
    }
}
