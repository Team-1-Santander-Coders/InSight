package getterson.insight.dtos;

import java.time.LocalDate;

public record SummaryDataDTO(String id,
                             LocalDate initialDate,
                             LocalDate finalDate,
                             String description,
                             String summary,
                             String image) {}