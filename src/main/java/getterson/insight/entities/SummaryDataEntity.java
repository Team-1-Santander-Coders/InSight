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

    @Column(nullable = false, length = 5000)
    private String summary;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String audio;

    @Column(name = "reference_data", nullable = false, length = 5000)
    @ElementCollection
    private List<String> referenceData;

    @ElementCollection
    private List<String> categories;

    @ManyToOne
    @JoinColumn (name = "topic_id")
    private TopicEntity topic;
}
