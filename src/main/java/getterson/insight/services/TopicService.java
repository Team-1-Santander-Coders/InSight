package getterson.insight.services;
import getterson.insight.entities.TopicEntity;
import getterson.insight.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public TopicEntity findById(Long id) throws Exception {
        Optional<TopicEntity> topicEntity = topicRepository.findById(id);
        if(topicEntity.isPresent()) return topicEntity.get();

        throw new Exception("Topico não encontrado");
    }

    public void save(TopicEntity topicEntity) {
        topicRepository.save(topicEntity);
    }
}
