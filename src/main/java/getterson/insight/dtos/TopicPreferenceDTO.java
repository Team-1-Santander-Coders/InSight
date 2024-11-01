package getterson.insight.dtos;

import getterson.insight.entities.types.PreferenceType;

public record TopicPreferenceDTO(
        long id,
        long userId,
        boolean sendNewsLetter,
        PreferenceType type
) {}
