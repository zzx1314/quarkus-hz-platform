package org.hzai.config;

import java.util.Map;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "hzfile")
public interface FileConfig {
    String basePath();

   Map<String, String> unknown();
}
