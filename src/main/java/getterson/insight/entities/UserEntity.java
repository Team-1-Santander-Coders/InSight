package getterson.insight.entities;

import getterson.insight.entities.types.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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




}
