package com.bsktpay.dfs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

	@Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor fileProcessingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("FileProc-");
        executor.initialize();
        return executor;
    }
}
