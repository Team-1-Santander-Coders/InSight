package getterson.insight.mappers;

import getterson.insight.dtos.TopicDTO;
import getterson.insight.entities.TopicEntity;

import getterson.insight.exceptions.TopicNotFoundException;
import getterson.insight.services.TopicService;
import getterson.insight.utils.exception.ThrowingFunctionWrapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopicMapper implements Mapper<TopicEntity, TopicDTO> {

    private final SummaryMapper summaryMapper;
    private final TopicService topicService;

    public TopicMapper(SummaryMapper summaryMapper, TopicService topicService) {
        this.topicService = topicService;
        this.summaryMapper = summaryMapper;
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
        return new TopicDTO(topicEntity.getId(), topicEntity.getTitle(), summaryMapper.toDTO(topicEntity.getSummaries()));
    }

    private TopicEntity getEntity(TopicDTO topicDTO) throws TopicNotFoundException {
        return topicService.findById(topicDTO.id());
    }
}
