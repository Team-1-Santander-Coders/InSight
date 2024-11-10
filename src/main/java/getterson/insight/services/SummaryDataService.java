package getterson.insight.services;

import getterson.insight.dtos.GeneratedSummary;
import getterson.insight.dtos.SummaryRequestDTO;
import getterson.insight.dtos.SummarySimpleDataDTO;
import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.entities.TopicEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.mappers.SummaryDataMapper;
import getterson.insight.repositories.SummaryDataRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class SummaryDataService {
    private final SummaryDataRepository summaryDataRepository;

    private static HashSet<SummaryRequestDTO> requestQueue = new HashSet<>();

    private static HashMap<SummaryRequestDTO, List<UserEntity>> usersToNotificate = new HashMap<>();

    public SummaryDataService(SummaryDataRepository summaryDataRepository, SummaryDataMapper summaryDataMapper, TopicRepository topicRepository) {
        this.summaryDataRepository = summaryDataRepository;
    }

    public void save(SummaryDataEntity summaryDataEntity) {
        summaryDataRepository.save(summaryDataEntity);
    }

    public Optional<SummaryDataEntity> findById(String id) {
        return summaryDataRepository.findById(id);
    }

    public boolean verifyOnQueue(SummaryRequestDTO summaryRequestDTO){
        return requestQueue.contains(summaryRequestDTO);
    }

    public void addToQueue(SummaryRequestDTO summaryRequest, UserEntity user) {
        Optional<TopicEntity> topicEntityOptional = topicRepository.findByTitle(summaryRequest.term());

        requestQueue.add(summaryRequest);
    }

    public void removeFromQueue(SummaryRequestDTO summaryRequestQueue){
        requestQueue.remove(summaryRequestQueue);
    }
}
