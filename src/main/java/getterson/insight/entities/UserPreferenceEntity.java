package getterson.insight.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_preferences")
public class UserPreferenceEntity extends PreferenceEntity {
    @ElementCollection
    private List<String> blackListWords;
}
