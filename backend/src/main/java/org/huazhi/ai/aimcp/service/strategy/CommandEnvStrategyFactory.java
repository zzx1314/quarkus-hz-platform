package org.huazhi.ai.aimcp.service.strategy;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class CommandEnvStrategyFactory {

	private final Map<String, CommandEnvStrategy> strategies;

	@Inject
	public CommandEnvStrategyFactory() {
		List<CommandEnvStrategy> strategyList = List.of(
				new JavaCommandEnvStrategy(),
				new PythonCommandEnvStrategy(),
				new NodeJsCommandEnvStrategy()
		);
		this.strategies = strategyList.stream()
				.collect(Collectors.toMap(
						strategy -> strategy.getClass().getSimpleName().replace("CommandEnvStrategy", "").toLowerCase(),
						strategy -> strategy
				));
	}

	public CommandEnvStrategy getStrategy(String commandType) {
		CommandEnvStrategy strategy = strategies.get(commandType.toLowerCase());
		if (strategy == null) {
			throw new IllegalArgumentException("Unsupported command type: " + commandType);
		}
		return strategy;
	}
}
