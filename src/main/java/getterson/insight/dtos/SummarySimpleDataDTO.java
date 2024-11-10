package getterson.insight.dtos;

import java.time.LocalDate;

public record SummarySimpleDataDTO(String id,
                                   LocalDate finalDate,
                                   LocalDate initialDate,
                                   String description) {}
