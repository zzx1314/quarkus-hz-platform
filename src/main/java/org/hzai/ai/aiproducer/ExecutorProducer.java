package org.hzai.ai.aiproducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ExecutorProducer {

    @Produces
    @ApplicationScoped
    public ExecutorService produceExecutorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
}

