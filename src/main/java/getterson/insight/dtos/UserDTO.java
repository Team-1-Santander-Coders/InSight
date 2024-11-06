package getterson.insight.dtos;

import java.util.List;

public record UserDTO(String name,
                      String username,
                      String document,
                      List<TopicPreferenceDTO> topicPreferences,
                      List<TopicDTO> topics) {}
