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
    private long id;

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
}
