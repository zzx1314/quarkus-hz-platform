package org.hzai.ai.aimcp.service.strategy;

import java.util.List;
import java.util.Map;

public interface CommandEnvStrategy {
	Map<String, String> getEnvironmentVariables();

	List<String> getCommand(List<String> commands);
}
