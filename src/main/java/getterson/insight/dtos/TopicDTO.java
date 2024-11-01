package getterson.insight.dtos;

import java.util.List;

public record TopicDTO(
        long id,
        String title,
        List<SummaryDTO> summaries
) {}
