package getterson.insight.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "summary_data")
public class SummaryDataEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private LocalDate initialDate;

    @Column(nullable = false)
    private LocalDate finalDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String audio;

    @Column(nullable = false)
    @ElementCollection
    private List<String> references;

    @ElementCollection
    private List<String> categories;

    @ManyToOne
    @JoinColumn (name = "topic_id")
    private TopicEntity topic;
}
