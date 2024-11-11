package getterson.insight.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeneratedSummary(String id,
                               String summary,
                               String description,
                               String categories,
                               @JsonProperty("image_url")
                               String imageUrl,
                               @JsonProperty("audio_url")
                               String audioUrl,
                               List<String> references) {}