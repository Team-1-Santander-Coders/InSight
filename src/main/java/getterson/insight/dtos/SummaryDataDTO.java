package getterson.insight.dtos;

import java.time.LocalDate;

public record SummaryDataDTO(
        long id,
        LocalDate date,
        String about,
        String details,
        String image
) {}
