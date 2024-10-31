package getterson.insight.dtos;

import getterson.insight.entities.SummaryDataEntity;

import java.time.LocalDate;

public record SummaryDataDTO(
        long id,
        LocalDate date,
        String about,
        String details,
        String image
) {
    //converte uma entidade
    public static SummaryDataDTO fromEntity(SummaryDataEntity summaryDataEntity) {
        return new SummaryDataDTO(
                summaryDataEntity.getId(),
                summaryDataEntity.getDate(),
                summaryDataEntity.getAbout(),
                summaryDataEntity.getDetails(),
                summaryDataEntity.getImage()
        );
    }
}
