package getterson.insight.services;

import getterson.insight.dtos.GeneratedSummary;
import getterson.insight.dtos.MessageRequestDTO;
import getterson.insight.dtos.SummaryRequestDTO;
import getterson.insight.dtos.SummarySimpleDataDTO;
import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.entities.TopicEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.mappers.SummaryDataMapper;
import getterson.insight.repositories.SummaryDataRepository;

import getterson.insight.repositories.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.*;

@Service
public class SummaryDataService {
    private final SummaryDataRepository summaryDataRepository;
    private final SummaryDataMapper summaryDataMapper;
    private final TopicRepository topicRepository;
    private final WebClient whatsappClient;
    private static final Logger logger = LoggerFactory.getLogger(SummaryDataService.class);

    private static final HashSet<SummaryRequestDTO> requestQueue = new HashSet<>();

    private static final HashMap<SummaryRequestDTO, List<UserEntity>> usersToNotificate = new HashMap<>();

    public SummaryDataService(SummaryDataRepository summaryDataRepository, SummaryDataMapper summaryDataMapper, TopicRepository topicRepository, WebClient whatsappClient) {
        this.summaryDataRepository = summaryDataRepository;
        this.summaryDataMapper = summaryDataMapper;
        this.topicRepository = topicRepository;
        this.whatsappClient = whatsappClient;
    }

    public void save(String topicTitle, LocalDate initialDate, LocalDate finalDate, GeneratedSummary generatedSummary, SummaryRequestDTO requestDTO) {
        SummaryDataEntity summaryDataEntity = summaryDataMapper.convertGeneratedSummaryToSummaryDataEntity(topicTitle, initialDate, finalDate, generatedSummary);
        removeFromQueue(requestDTO);
        if (usersToNotificate.containsKey(requestDTO)) sendNotificationsToUsers(requestDTO);

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

        if (user.getUserPreference().isSendNotificationWhenReady()) {
            List<UserEntity> userList;
            if (usersToNotificate.containsKey(summaryRequest)) userList = usersToNotificate.get(summaryRequest);
            else userList = new ArrayList<>();
            userList.add(user);
            usersToNotificate.put(summaryRequest, userList);
        }

        requestQueue.add(summaryRequest);
    }

    public void removeFromQueue(SummaryRequestDTO summaryRequest){
        requestQueue.remove(summaryRequest);
    }

    public void sendNotificationsToUsers(SummaryRequestDTO summaryRequestDTO) {
        List<UserEntity> userEntityList = usersToNotificate.get(summaryRequestDTO);

        String message = String.format("Olá, {NOME_USUARIO}!\nSeu InSight sobre o tópico \"%s\" já está pronto!", summaryRequestDTO.term());

        userEntityList.forEach(user ->
                whatsappClient.post()
                        .uri("/sendmessage")
                        .bodyValue(new MessageRequestDTO(user.getPhone(), message.replace("{NOME_USUARIO}", user.getName())))
                        .retrieve()
                        .bodyToMono(String.class)
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe(response -> userEntityList.remove(user),
                                error -> logger.error("Erro ao tentar enviar mensagem para o usuário de ID {}: {}", user.getId(), error.getMessage())
                ));

        usersToNotificate.remove(summaryRequestDTO);
    }


}
