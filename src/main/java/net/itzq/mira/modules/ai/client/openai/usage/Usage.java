package net.itzq.mira.modules.ai.client.openai.usage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Usage
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usage implements Serializable {
    @JsonProperty("prompt_tokens")
    private long promptTokens = 0L;
    @JsonProperty("completion_tokens")
    private long completionTokens = 0L;
    @JsonProperty("total_tokens")
    private long totalTokens = 0L;
}
