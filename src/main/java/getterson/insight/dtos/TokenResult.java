package getterson.insight.dtos;

import getterson.insight.entities.TokenEntity;

public record TokenResult(TokenEntity token, boolean isExpired) {}
