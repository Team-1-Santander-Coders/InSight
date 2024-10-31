package getterson.insight.dtos;

import getterson.insight.entities.SummaryEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record SummaryDTO(
        long id,
        List<String> categories,
        List<SummaryDataDTO> summaryDataDto,
        String topicTitle
) {

    public static SummaryDTO fromEntity(SummaryEntity summaryEntity) {
        return new SummaryDTO(
                summaryEntity.getId(),
                summaryEntity.getCategories(),
                summaryEntity.getSummaryData() != null ?
                        summaryEntity.getSummaryData().stream()
                                .map(SummaryDataDTO::fromEntity)
                                .collect(Collectors.toList()) :
                        Collections.emptyList(),
                summaryEntity.getTopic() != null ? summaryEntity.getTopic().getTitle() : null
        );
    }
}
