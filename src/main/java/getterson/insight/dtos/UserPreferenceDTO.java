package getterson.insight.dtos;

import getterson.insight.entities.types.PreferenceType;

import java.util.List;

public record UserPreferenceDTO(
        long id,
        long userId,
        List<String> blackListWords,
        PreferenceType type
) {}
