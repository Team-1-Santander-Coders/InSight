package getterson.insight.dtos;

import java.util.List;


public record SummaryDTO(Long id,
                         List<String> categories,
                         List<SummaryDataDTO> summaryDataDto,
                         String topicTitle) {}
