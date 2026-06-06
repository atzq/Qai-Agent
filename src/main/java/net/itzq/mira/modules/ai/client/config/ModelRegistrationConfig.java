package net.itzq.mira.modules.ai.client.config;

import lombok.Builder;
import lombok.Data;
import net.itzq.mira.modules.ai.client.handle.HttpStreamEventInterface;

import java.util.Map;

/**
 *  ModelRegistration
 *
 *  @author tangzq
 */
@Data
@Builder
public class ModelRegistrationConfig {

    private final String alias;

    private final String providerName;

    private final String apiModelName;

    private final String apiHost;

    private final String apiKey;

    private final String apiEndpoint;

    private final Map<String, String> apiHeaders;

    Class<? extends HttpStreamEventInterface> sseEventHandler;

}
