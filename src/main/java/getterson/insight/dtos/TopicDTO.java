package getterson.insight.dtos;

import java.util.List;

public record TopicDTO(Long id,
                       String title,
                       List<SummaryDTO> summaries) {}
