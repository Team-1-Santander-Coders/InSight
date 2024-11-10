package getterson.insight.dtos;

import java.time.LocalDate;
import java.util.List;

public record SummaryDataDTO(String id,
                             LocalDate initialDate,
                             LocalDate finalDate,
                             String description,
                             String summary,
                             String imageUrl,
                             String audioUrl,
                             List<String> references,
                             List<String> categories) {}