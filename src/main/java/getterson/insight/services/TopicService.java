package getterson.insight.services;
import getterson.insight.entities.TopicEntity;
import getterson.insight.exceptions.TopicNotFoundException;
import getterson.insight.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public TopicEntity findByTitle(String title) throws TopicNotFoundException {
        Optional<TopicEntity> topicEntity = topicRepository.findByTitle(title);
        if(topicEntity.isPresent()) return topicEntity.get();

        throw new TopicNotFoundException(String.format("Tópico com o título \"%s\" não encontrado.", title));
    }

    public List<TopicEntity> findAll() {
        return topicRepository.findAll();
    }

    public TopicEntity findById(Long id) throws TopicNotFoundException {
        Optional<TopicEntity> topicEntity = topicRepository.findById(id);
        if(topicEntity.isPresent()) return topicEntity.get();

        throw new TopicNotFoundException(String.format("Tópico com o id %d não encontrado.", id));
    }

    public TopicEntity save(TopicEntity topicEntity) {
        return topicRepository.saveAndFlush(topicEntity);
    }

    public List<TopicEntity> saveAll(List<TopicEntity> topicEntities) {
        return topicRepository.saveAllAndFlush(topicEntities);
    }
}
