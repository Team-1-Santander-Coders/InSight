package getterson.insight.dtos;

import getterson.insight.entities.types.PreferenceType;

public record TopicPreferenceDTO(Long id,
                                 Long userId,
                                 boolean sendNewsLetter,
                                 PreferenceType type) {}
