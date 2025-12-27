package org.huazhi.ai.aimcp.service.strategy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaCommandEnvStrategy implements CommandEnvStrategy {
	private final String JAVA_HOME = "/home/zzx/.jdks/corretto-17.0.15/";
	@Override
	public Map<String, String> getEnvironmentVariables() {
		Map<String, String> env = new HashMap<>();
		env.put("JAVA_HOME", JAVA_HOME);
		return env;
	}

	@Override
	public List<String> getCommand(List<String> commands) {
		List<String> newCommands = new ArrayList<>();
		newCommands.add(JAVA_HOME + "bin/java");
		newCommands.add("-Dfile.encoding=UTF-8");
		newCommands.add("-jar");
		newCommands.add("$MCP_FILE_PATH");
		return newCommands;
	}
}
