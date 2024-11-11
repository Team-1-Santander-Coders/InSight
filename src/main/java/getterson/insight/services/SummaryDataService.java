package getterson.insight.services;

import getterson.insight.dtos.GeneratedSummary;
import getterson.insight.dtos.MessageRequestDTO;
import getterson.insight.dtos.SummaryRequestDTO;

import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.entities.TopicEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.mappers.SummaryDataMapper;
import getterson.insight.repositories.SummaryDataRepository;

import getterson.insight.repositories.TopicRepository;
import getterson.insight.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.*;

import static getterson.insight.utils.DateUtil.ISO8601_DATE_PATTERN;
import static getterson.insight.utils.DateUtil.stringToDate;

@Service
public class SummaryDataService {
    private final SummaryDataRepository summaryDataRepository;
    private final SummaryDataMapper summaryDataMapper;
    private final TopicRepository topicRepository;
    private final WebClient whatsappClient;
    private final WebClient summarizeClient;
    private static final Logger logger = LoggerFactory.getLogger(SummaryDataService.class);

    private static final HashSet<SummaryRequestDTO> requestQueue = new HashSet<>();

    private static final HashMap<SummaryRequestDTO, List<UserEntity>> usersToNotificate = new HashMap<>();

    public SummaryDataService(SummaryDataRepository summaryDataRepository, SummaryDataMapper summaryDataMapper, TopicRepository topicRepository, WebClient whatsappClient, WebClient summarizeClient, SummaryService summaryService) {
        this.summaryDataRepository = summaryDataRepository;
        this.summaryDataMapper = summaryDataMapper;
        this.topicRepository = topicRepository;
        this.whatsappClient = whatsappClient;
        this.summarizeClient = summarizeClient;
    }

    public void save(String topicTitle, LocalDate initialDate, LocalDate finalDate, GeneratedSummary generatedSummary, SummaryRequestDTO requestDTO) {
        SummaryDataEntity summaryDataEntity = summaryDataMapper.convertGeneratedSummaryToSummaryDataEntity(topicTitle, initialDate, finalDate, generatedSummary);

        removeFromQueue(requestDTO);
        if (usersToNotificate.containsKey(requestDTO)) sendNotificationsToUsers(requestDTO, summaryDataEntity.getAudio());

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
        if(!verifyIfSummmaryDataIsIncluded(summaryRequest.term(), DateUtil.stringToDate(summaryRequest.start_date(), ISO8601_DATE_PATTERN), DateUtil.stringToDate(summaryRequest.end_date(), ISO8601_DATE_PATTERN))) {
            if (user.getUserPreference().isSendNotificationWhenReady()) {
                List<UserEntity> userList;
                if (usersToNotificate.containsKey(summaryRequest)) userList = usersToNotificate.get(summaryRequest);
                else userList = new ArrayList<>();
                userList.add(user);
                usersToNotificate.put(summaryRequest, userList);
            }
            requestQueue.add(summaryRequest);
        }
    }

    public void removeFromQueue(SummaryRequestDTO summaryRequest){
        requestQueue.remove(summaryRequest);
    }

    public void sendNotificationsToUsers(SummaryRequestDTO summaryRequestDTO, String audioUrl) {
        List<UserEntity> userEntityList = usersToNotificate.get(summaryRequestDTO);

        String message = String.format("Olá, {NOME_USUARIO}!\n\nSeu InSight sobre o tópico \"%s\" já está pronto!\n\nConfira o podcast disponível sobre em: %s", summaryRequestDTO.term(), audioUrl);

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

    @Scheduled(fixedDelay = 5000)
    public void processQueue() {
        if (!requestQueue.isEmpty()) {
            for (SummaryRequestDTO requestDTO : new HashSet<>(requestQueue)) {
                collectData(
                        requestDTO.term(),
                        requestDTO.start_date(),
                        requestDTO.end_date()
                );
            }
        }
    }

    private boolean verifyIfSummmaryDataIsIncluded(String topicTitle, LocalDate initialDate, LocalDate finalDate){
        Optional<SummaryDataEntity> summaryDataEntityOptional = summaryDataRepository.findByTopicTitleAndInitialDateAndFinalDate(topicTitle, initialDate, finalDate);
        return summaryDataEntityOptional.isPresent();
    }

    private void collectData(String topicTitle, String initialDate, String finalDate) {
        SummaryRequestDTO summaryRequest = new SummaryRequestDTO(topicTitle, initialDate, finalDate);
        summarizeClient.post()
                .uri("/summarize")
                .bodyValue(summaryRequest)
                .retrieve()
                .bodyToMono(GeneratedSummary.class)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        summarizedData -> save(topicTitle, stringToDate(initialDate, ISO8601_DATE_PATTERN), stringToDate(finalDate, ISO8601_DATE_PATTERN), summarizedData, summaryRequest),
                        error -> logger.error("Erro ao tentar obter o resumo para o tópico {}: {}", topicTitle, error.getMessage()));
    }
}
