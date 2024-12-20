package getterson.insight.mappers;

import getterson.insight.dtos.TopicDTO;
import getterson.insight.entities.TopicEntity;

import getterson.insight.exceptions.TopicNotFoundException;
import getterson.insight.services.TopicService;
import getterson.insight.utils.exception.ThrowingFunctionWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TopicMapper implements Mapper<TopicEntity, TopicDTO> {

    private final TopicService topicService;
    private final SummaryDataMapper summaryDataMapper;

    public TopicMapper(TopicService topicService, SummaryDataMapper summaryDataMapper) {
        this.topicService = topicService;
        this.summaryDataMapper = summaryDataMapper;
    }

    public TopicDTO toDTO(TopicEntity topicEntity){
        return createDTO(topicEntity);
    }


    public List<TopicDTO> toDTO(List<TopicEntity> topicEntities){
        return topicEntities.stream()
                .map(this::createDTO)
                .toList();
    }

    public TopicEntity toEntity(TopicDTO topicDTO) throws TopicNotFoundException {
        return getEntity(topicDTO);
    }

    public List<TopicEntity> toEntity(List<TopicDTO> topicDTOS) {
        return topicDTOS.stream()
                .map(ThrowingFunctionWrapper.wrap(this::getEntity))
                .toList();
    }

    private TopicDTO createDTO(TopicEntity topicEntity) {
        if(topicEntity.getSummaries() == null) return new TopicDTO(topicEntity.getId(), topicEntity.getTitle(), new ArrayList<>());
        return new TopicDTO(topicEntity.getId(), topicEntity.getTitle(), summaryDataMapper.toSimpleDTO(topicEntity.getSummaries()));
    }


    private TopicEntity getEntity(TopicDTO topicDTO) throws TopicNotFoundException {
        return topicService.findById(topicDTO.id());
    }
}
