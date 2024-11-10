package getterson.insight;


import getterson.insight.entities.*;
import getterson.insight.entities.types.PreferenceType;

import getterson.insight.mappers.TopicPreferenceMapper;
import getterson.insight.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import getterson.insight.mappers.UserPreferenceMapper;
import java.time.LocalDate;
import java.util.List;

@Component
public class Tests {

    @Autowired
    UserService userService;
    @Autowired
    TopicService topicService;
    @Autowired
    SummaryService summaryService;
    @Autowired
    SummaryDataService summaryDataService;
    @Autowired
    UserPreferenceService userPreferenceService;
    @Autowired
    TopicPreferenceService topicPreferenceService;
    @Autowired
    UserPreferenceMapper userPreferenceMapper;
    @Autowired
    TopicPreferenceMapper topicPreferenceMapper;
    public void execute() throws Exception {
        //userService.registerUser("Matheus Lima Moreira", "mathlimam", "07424488511", LocalDate.now(), "math@math.com", "Alicein@99");

        //TopicEntity topicEntity = new TopicEntity("Python não é tão bom assim", userService.getUserByUsername("mathlimam"));
        //topicService.save(topicEntity);
        //SummaryEntity summaryEntity = new SummaryEntity(List.of("SEI LA MAN, N ME ENCHE"), topicService.findById(1L).get());
        //summaryService.saveSummary(summaryEntity);
        //SummaryDataEntity summaryDataEntity = new SummaryDataEntity(summaryService.findSummaryById(1L), LocalDate.now(), "Hoje", "É hoje", "Ihh", "Ahh");
        //summaryDataService.save(summaryDataEntity);
        //UserPreferenceEntity userPreferenceEntity = new UserPreferenceEntity(List.of("Javascript"), userService.getUserByUsername("mathlimam"), PreferenceType.USER);
        //TopicPreferenceEntity topicPreferenceEntity = new TopicPreferenceEntity(userService.getUserByUsername("mathlimam"), topicService.findById(1L));

        //userPreferenceService.createUserPreferenceEntity(userService.getUserByUsername("mathlimam"));
        //userPreferenceService.updateUserPreference(userPreferenceMapper.toDTO(userPreferenceEntity));
        //topicPreferenceService.updateTopicPreference(topicPreferenceMapper.toDTO(topicPreferenceEntity));

    }
}
