package org.huazhi.ai.aimcp.service.strategy;


import java.util.List;
import java.util.Map;

public class PythonCommandEnvStrategy implements CommandEnvStrategy {
	@Override
	public Map<String, String> getEnvironmentVariables() {
		return Map.of();
	}

	@Override
	public List<String> getCommand(List<String> commands) {
		return List.of();
	}
}
