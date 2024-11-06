package getterson.insight.mappers;

import getterson.insight.dtos.SummaryDTO;
import getterson.insight.dtos.TopicDTO;
import getterson.insight.entities.TopicEntity;

import getterson.insight.mappers.Mapper;
import getterson.insight.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TopicMapper implements Mapper<TopicEntity, TopicDTO> {

    @Autowired
    private  SummaryMapper summaryMapper;

    @Autowired
    private TopicService topicService;

    public TopicDTO toDTO(TopicEntity topicEntity){
        return new TopicDTO(topicEntity.getId(), topicEntity.getTitle(), summaryMapper.toDTO(topicEntity.getSummaries()));
    }

    public TopicEntity toEntity (TopicDTO topicDTO) throws Exception {
        return topicService.findById(topicDTO.id());
    }
}
