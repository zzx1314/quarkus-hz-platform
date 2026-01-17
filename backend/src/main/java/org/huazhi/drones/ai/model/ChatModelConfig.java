package org.huazhi.drones.ai.model;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "aimodel")
public interface ChatModelConfig {
    String baseUrl();

    String apiKey();

    String modelName();
}
