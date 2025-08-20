package org.hzai.ai.aidocument.service.factory;

import org.hzai.ai.aidocument.service.strategy.*;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class DocumentSplitterFactory {
    private final Map<String, StrategyProvider> strategyMap = new HashMap<>();

    public DocumentSplitterFactory() {
        // 固定分段策略
        strategyMap.put("characterSplitter", params -> {
            int segmentSize = getOrDefault(params, "segmentSize", 500);
			int overlapSize = getOrDefault(params, "overlapSize", 50);
            return new CharacterDocumentSplittingStrategy(segmentSize, overlapSize);
         }
        );
        // 添加正则表达式分割策略
        strategyMap.put("regexSplitter", params -> new RegexDocumentSplittingStrategy());
        // 添加标题分割策略
        strategyMap.put("headingSplitter", params -> new WordHeadingSplittingStrategy());
        // 添加所有内容分割策略
        strategyMap.put("allWorkSplitter", params -> new AllWorkSplitterStrategy());
        // 添加按照标识进行分割策略
        strategyMap.put("flagSplitter", params -> new FlagDocumentSplittingStrategy(
                params.get("flag").toString()
        ));
    }

    public DocumentSplittingStrategy getStrategy(String strategyName, Map<String, Object> params) {
        StrategyProvider provider = strategyMap.get(strategyName);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown strategy: " + strategyName);
        }
        return provider.get(params);
    }

    @FunctionalInterface
    private interface StrategyProvider {
        DocumentSplittingStrategy get(Map<String, Object> params);
    }

    private static int getOrDefault(Map<String, Object> params, String key, int defaultValue) {
		Object value = params.get(key);
		if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof String) {
			try {
				return Integer.parseInt((String) value);
			} catch (NumberFormatException ignored) {}
		}
		return defaultValue;
	}
}
