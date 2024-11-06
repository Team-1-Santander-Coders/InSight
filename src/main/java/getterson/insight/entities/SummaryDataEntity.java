package getterson.insight.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "summary_data")
public class SummaryDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "summary_id")
    private SummaryEntity summary;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String about;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String audio;

    public SummaryDataEntity(SummaryEntity summaryEntity, LocalDate date, String about, String details, String image, String audio) {
        this.summary = summaryEntity;
        this.date = date;
        this.about = about;
        this.details = details;
        this.image = image;
        this.audio = audio;
    }
}
