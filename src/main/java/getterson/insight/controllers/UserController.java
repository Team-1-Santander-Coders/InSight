package getterson.insight.controllers;

import getterson.insight.dtos.*;
import getterson.insight.entities.*;
import getterson.insight.entities.types.PreferenceType;
import getterson.insight.mappers.SummaryDataMapper;
import getterson.insight.repositories.*;
import getterson.insight.services.*;
import getterson.insight.utils.DateUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class UserController {
    private final TopicRepository topicRepository;
    private final CollectorService collectorService;
    private final UserRepository userRepository;
    private final SummaryDataService summaryDataService;
    private final UserPreferenceRepository userPreferenceRepository;
    private final TopicPreferenceRepository topicPreferenceRepository;
    private final UserService userService;
    private final SummaryDataMapper summaryDataMapper;


    public UserController(TopicRepository topicRepository, CollectorService collectorService, UserRepository userRepository, SummaryDataService summaryDataService, UserPreferenceRepository userPreferenceRepository, TopicPreferenceRepository topicPreferenceRepository, UserService userService, SummaryDataMapper summaryDataMapper) {
        this.topicRepository = topicRepository;
        this.collectorService = collectorService;
        this.userRepository = userRepository;
        this.summaryDataService = summaryDataService;
        this.userPreferenceRepository = userPreferenceRepository;
        this.topicPreferenceRepository = topicPreferenceRepository;
        this.userService = userService;
        this.summaryDataMapper = summaryDataMapper;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserTopicAndPreferenceList(){
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(List.of(authenticatedUser.getTopicList(), authenticatedUser.getUserPreference(), authenticatedUser.getTopicPreferenceList()));
    }

    @GetMapping("/summaries")
    public ResponseEntity<?> getUserSummaryList(){
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<List<SummarySimpleDataDTO>> summarySimpleDataDTOList = userService.findAllAsSimpleDTO(authenticatedUser.getId());
        
        if(summarySimpleDataDTOList.isPresent()) return ResponseEntity.ok(summarySimpleDataDTOList.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lista de resumos não encontrada");
    }

    @GetMapping("/summary/{id}")
    public ResponseEntity<?> getSummary(@PathVariable("id") String id){
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<SummaryDataEntity> summaryDataOpt = summaryDataService.findById(id);

        if (summaryDataOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este resumo não existe.");

        SummaryDataEntity summaryData = summaryDataOpt.get();

        boolean isInUserTopicList = authenticatedUser.getTopicList()
                .stream()
                .anyMatch(topic -> topic.equals(summaryData.getTopic()));

        if (isInUserTopicList) return ResponseEntity.ok(summaryDataMapper.toDTO(summaryData));
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Resumo não pode ser acessado por este usuário.");
    }

    @PostMapping("/topic")
    public ResponseEntity<?> addTopicToUser(@RequestBody TopicRequestDTO topicRequestDTO) {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<TopicEntity> topicEntity = topicRepository.findByTitle(topicRequestDTO.title());

        if (topicEntity.isEmpty())
            return ResponseEntity.ok(topicRepository.saveAndFlush(new TopicEntity(topicRequestDTO.title(), authenticatedUser)));
        else {
            userRepository.findByDocument(authenticatedUser.getDocument()).ifPresent(user -> {
                user.addTopic(topicEntity.get());
            });
            return ResponseEntity.ok(topicEntity);
        }
    }

    @PostMapping("/summarize")
    public ResponseEntity<?> summarizeTopic(@RequestParam Long topicId, @RequestParam String period) {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<TopicEntity> topicEntity = topicRepository.findById(topicId);

        if (topicEntity.isPresent()) {
            SummaryRequestDTO summaryRequestDTO = createSummaryDataDTOByPeriod(topicEntity.get(), period);

            if (!summaryDataService.verifyOnQueue(summaryRequestDTO)) {
                summaryDataService.addToQueue(summaryRequestDTO, authenticatedUser);

                return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Solicitação bem-sucedida.");
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(102)).body("Esta requisição já está sendo processada pelo servidor e seu resultado estará disponível o mais rápido possível.");
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(503)).body("Serviço temporariamente indisponível.");
    }

    @PostMapping("/preference/{preferenceType}")
    public ResponseEntity<?> setPreference(@PathVariable("preferenceType") PreferenceType preferenceType,
                                           @RequestParam(required = false) Long topicId,
                                           @RequestParam(required = false) boolean sendNewsletter,
                                           @RequestParam(required = false) boolean sendNotificationWhenReady) {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (preferenceType.equals(PreferenceType.USER)) {
            UserPreferenceEntity userPreferenceEntity = new UserPreferenceEntity(authenticatedUser);
            userPreferenceEntity.setSendNotificationWhenReady(sendNotificationWhenReady);
            userPreferenceRepository.saveAndFlush(userPreferenceEntity);

            return ResponseEntity.ok(userPreferenceEntity);
        }

        if (preferenceType.equals(PreferenceType.TOPIC)) {
            Optional<TopicEntity> topicEntity = topicRepository.findById(topicId);
            if (topicEntity.isPresent()) {
                TopicPreferenceEntity topicPreferenceEntity = new TopicPreferenceEntity(authenticatedUser, topicEntity.get());
                topicPreferenceEntity.setSendNewsLetter(sendNewsletter);
                topicPreferenceRepository.saveAndFlush(topicPreferenceEntity);

                return ResponseEntity.ok(topicPreferenceEntity);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico não encontrado");
    }

    private static SummaryRequestDTO createSummaryDataDTOByPeriod (TopicEntity topicEntity, String period){
        if (period == null | period.isBlank()) {
            return new SummaryRequestDTO(topicEntity.getTitle(),
                    DateUtil.dateToIsoString(LocalDate.now().minusDays(1)),
                    DateUtil.dateToString(LocalDate.now()));
        }

        if (period.equals("week")) {
            return new SummaryRequestDTO(topicEntity.getTitle(),
                    DateUtil.dateToIsoString(LocalDate.now().minusDays(7)),
                    DateUtil.dateToString(LocalDate.now()));
        }

        if (period.equals("month")) {
            return new SummaryRequestDTO(topicEntity.getTitle(),
                    DateUtil.dateToIsoString(LocalDate.now().minusDays(7)),
                    DateUtil.dateToString(LocalDate.now()));
        }

        return null;
    }


}