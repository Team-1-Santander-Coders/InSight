package getterson.insight.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeneratedSummaryDTO(long id,
                                  String summary,
                                  String description,
                                  @JsonProperty("image_url")
                                  String imageUrl,
                                  @JsonProperty("audio_url")
                                  String audioUrl) {}
