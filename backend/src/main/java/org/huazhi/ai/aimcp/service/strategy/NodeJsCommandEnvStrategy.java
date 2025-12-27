package org.huazhi.ai.aimcp.service.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeJsCommandEnvStrategy  implements CommandEnvStrategy {
	@Override
	public Map<String, String> getEnvironmentVariables() {
		Map<String, String> env = new HashMap<>();
		env.put("NODE_ENV", "production");
		env.put("NPM_CONFIG_PREFIX", "/opt/npm-global");
		return env;
	}

	@Override
	public List<String> getCommand(List<String> commands) {
		return List.of();
	}
}
