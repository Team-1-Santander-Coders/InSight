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
    private long id;

    @ElementCollection
    private List<String> categories;

    @OneToMany(mappedBy = "summary", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SummaryDataEntity> summaryData;

    @ManyToOne
    @JoinColumn (name = "topic_id")
    private TopicEntity topic;

    
}
