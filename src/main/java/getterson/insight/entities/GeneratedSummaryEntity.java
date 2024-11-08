package getterson.insight.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "generated_summary")
public class GeneratedSummaryEntity {
    @Id
    private long id;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imageURL;

    private String audioURL = "none";

    public GeneratedSummaryEntity(long id, String summary, String description, String imageURL) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.imageURL = imageURL;
    }
}
