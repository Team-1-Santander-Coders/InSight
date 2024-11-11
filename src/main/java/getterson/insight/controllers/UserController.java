package getterson.insight.controllers;

import getterson.insight.dtos.*;
import getterson.insight.entities.*;
import getterson.insight.entities.types.PreferenceType;
import getterson.insight.exceptions.InvalidPeriodException;
import getterson.insight.mappers.SummaryDataMapper;
import getterson.insight.mappers.TopicMapper;
import getterson.insight.mappers.TopicPreferenceMapper;
import getterson.insight.mappers.UserPreferenceMapper;
import getterson.insight.repositories.*;
import getterson.insight.services.*;
import getterson.insight.utils.DateUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;


@RestController
public class UserController {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final UserRepository userRepository;
    private final SummaryDataService summaryDataService;
    private final UserPreferenceRepository userPreferenceRepository;
    private final TopicPreferenceRepository topicPreferenceRepository;
    private final TopicPreferenceService topicPreferenceService;
    private final UserService userService;
    private final SummaryDataMapper summaryDataMapper;
    private final UserPreferenceMapper userPreferenceMapper;
    private final TopicPreferenceMapper topicPreferenceMapper;


    public UserController(TopicRepository topicRepository, TopicMapper topicMapper, UserRepository userRepository, SummaryDataService summaryDataService, UserPreferenceRepository userPreferenceRepository, TopicPreferenceRepository topicPreferenceRepository, TopicPreferenceService topicPreferenceService, UserService userService, SummaryDataMapper summaryDataMapper, UserPreferenceMapper userPreferenceMapper, TopicPreferenceMapper topicPreferenceMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
        this.userRepository = userRepository;
        this.summaryDataService = summaryDataService;
        this.userPreferenceRepository = userPreferenceRepository;
        this.topicPreferenceRepository = topicPreferenceRepository;
        this.topicPreferenceService = topicPreferenceService;
        this.userService = userService;
        this.summaryDataMapper = summaryDataMapper;
        this.userPreferenceMapper = userPreferenceMapper;
        this.topicPreferenceMapper = topicPreferenceMapper;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserTopicAndPreferenceList(){
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(List.of(topicMapper.toDTO(authenticatedUser.getTopicList()),
                                         userPreferenceMapper.toDTO(authenticatedUser.getUserPreference()),
                                         topicPreferenceMapper.toDTO(authenticatedUser.getTopicPreferenceList())));
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
                .anyMatch(topic -> topic.getSummaries()
                                      .stream()
                                      .anyMatch(summary -> summary.getId().equals(summaryData.getId())));

        if (isInUserTopicList) return ResponseEntity.ok(summaryDataMapper.toDTO(summaryData));
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Resumo não pode ser acessado por este usuário.");
    }

    @PostMapping("/topic")
    public ResponseEntity<?> addTopicToUser(@RequestBody TopicRequestDTO topicRequestDTO) {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<TopicEntity> topicEntityOptional = topicRepository.findByTitle(topicRequestDTO.title());
        TopicEntity topic;

        if (topicEntityOptional.isPresent()) {
            topic = topicEntityOptional.get();
            authenticatedUser.addTopic(topic);
        }
        else {
            topic = topicRepository.saveAndFlush(new TopicEntity(topicRequestDTO.title(), authenticatedUser));
        }

        SummaryRequestDTO summaryRequestDTO = createSummaryDataDTOByPeriod(topic, "day");

        if (summaryRequestDTO != null) summaryDataService.addToQueue(summaryRequestDTO, authenticatedUser);

        TopicPreferenceEntity topicPreference = topicPreferenceService.createTopicPreference(authenticatedUser, topic);

        authenticatedUser.addTopicPreference(topicPreference);
        userRepository.saveAndFlush(authenticatedUser);

        return ResponseEntity.ok(topicMapper.toDTO(topic));
    }

    @PostMapping("/summarize")
    public ResponseEntity<?> summarizeTopic(@RequestBody SummarizeRequestDTO summarizeRequestDTO) {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<TopicEntity> topicEntity = topicRepository.findById(Long.parseLong(summarizeRequestDTO.topic_id()));

        try {
            if (topicEntity.isPresent()) {
                SummaryRequestDTO summaryRequestDTO = createSummaryDataDTOByPeriod(topicEntity.get(), summarizeRequestDTO.period());
                if (summaryRequestDTO == null) throw new InvalidPeriodException();

                if (!summaryDataService.verifyOnQueue(summaryRequestDTO)) {
                    summaryDataService.addToQueue(summaryRequestDTO, authenticatedUser);
                    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Solicitação bem-sucedida.");
                }
                return ResponseEntity.status(HttpStatusCode.valueOf(102)).body("Esta requisição já está sendo processada pelo servidor e seu resultado estará disponível o mais rápido possível.");
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(503)).body("Serviço temporariamente indisponível.");
        } catch (InvalidPeriodException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/preference/{preferenceType}")
    public ResponseEntity<?> setPreference(@PathVariable("preferenceType") PreferenceType preferenceType,
                                           @RequestParam(required = false, value = "topic_id") Long topicId,
                                           @RequestParam(required = false, value = "send_newsletter") boolean sendNewsletter,
                                           @RequestParam(required = false, value = "send_when_ready") boolean sendNotificationWhenReady) throws Exception {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (preferenceType.equals(PreferenceType.USER)) {
            authenticatedUser.getUserPreference().setSendNotificationWhenReady(sendNotificationWhenReady);
            return ResponseEntity.ok(userPreferenceMapper.toDTO(authenticatedUser.getUserPreference()));
        }

        if (preferenceType.equals(PreferenceType.TOPIC)) {

            Optional<TopicEntity> topicEntityOptional = topicRepository.findById(topicId);
            if(topicEntityOptional.isPresent()) {
                TopicEntity topicEntity = topicEntityOptional.get();
                Optional<TopicPreferenceEntity> topicPreferenceEntityOptional = topicPreferenceService.getTopicPreferenceByTopicAndUser(topicEntity, authenticatedUser);
                if (topicPreferenceEntityOptional.isPresent()) {
                    TopicPreferenceEntity topicPreferenceEntity = topicPreferenceEntityOptional.get();
                    topicPreferenceEntity.setSendNewsLetter(sendNewsletter);
                    topicPreferenceService.updateTopicPreference(topicPreferenceMapper.toDTO(topicPreferenceEntity));
                    return ResponseEntity.ok(topicPreferenceMapper.toDTO(topicPreferenceEntity));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico não encontrado");
    }

    private static SummaryRequestDTO createSummaryDataDTOByPeriod(TopicEntity topicEntity, String period){

        if (period.equalsIgnoreCase("week")) {
            return new SummaryRequestDTO(topicEntity.getTitle(),
                    DateUtil.dateToIsoString(LocalDate.now().minusDays(7)),
                    DateUtil.dateToIsoString(LocalDate.now()));
        }

        if (period.equalsIgnoreCase("month")) {
            return new SummaryRequestDTO(topicEntity.getTitle(),
                    DateUtil.dateToIsoString(LocalDate.now().minusDays(30)),
                    DateUtil.dateToIsoString(LocalDate.now()));
        }

        if (period.equalsIgnoreCase("day")) {
            return new SummaryRequestDTO(topicEntity.getTitle(),
                    DateUtil.dateToIsoString(LocalDate.now().minusDays(1)),
                    DateUtil.dateToIsoString(LocalDate.now()));
        }

        return null;
    }


}