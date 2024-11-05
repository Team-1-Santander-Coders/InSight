package getterson.insight.dtos;

import java.time.LocalDate;

public record SummaryDataDTO(Long id,
                             LocalDate date,
                             String about,
                             String details,
                             String image) {}
