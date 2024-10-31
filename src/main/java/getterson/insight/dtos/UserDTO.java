package getterson.insight.dtos;

import java.util.List;

public record UserDTO(long id,
                      String name,
                      String username,
                      List<PreferenceDTO> preferences,
                      List<TopicDTO> topics) {}
