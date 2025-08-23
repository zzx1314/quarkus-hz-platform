package org.hzai.ai.aimcp.service;

import dev.langchain4j.mcp.client.McpClient;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class MCPClientRegistry {
	private final Map<Long, McpClient> clientMap = new ConcurrentHashMap<>();


	public void register(Long id, McpClient client) {
		clientMap.put(id, client);
	}

	public McpClient get(Long id) {
		return clientMap.get(id);
	}

	public Collection<McpClient> getAll() {
		return clientMap.values();
	}

	public boolean contains(Long name) {
		return clientMap.containsKey(name);
	}
}
