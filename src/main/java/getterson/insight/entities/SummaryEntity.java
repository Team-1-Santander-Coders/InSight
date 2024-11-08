package getterson.insight.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "summaries")
public class SummaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> categories;

    @OneToMany(mappedBy = "summary", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SummaryDataEntity> summaryDataList;

    @ManyToOne
    @JoinColumn (name = "topic_id")
    private TopicEntity topic;

    public SummaryEntity(List<String> categories, TopicEntity topic) {
        this.categories = categories;
        this.topic = topic;
    }
}
