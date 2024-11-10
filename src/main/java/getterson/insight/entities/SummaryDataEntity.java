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
    private String id;

    @ManyToOne
    @JoinColumn(name = "summary_id")
    private SummaryEntity summary;

    @Column(nullable = false)
    private LocalDate initialDate;

    @Column(nullable = false)
    private LocalDate finalDate;

    @Column(nullable = false)
    private String about;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String audio;

    @Column
    private boolean isAudioGenerated = false;

    public SummaryDataEntity(SummaryEntity summaryEntity, LocalDate initialDate, LocalDate finalDate, String about, String details, String image, String audio) {
        this.summary = summaryEntity;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.about = about;
        this.details = details;
        this.image = image;
        this.audio = audio;
    }
}
