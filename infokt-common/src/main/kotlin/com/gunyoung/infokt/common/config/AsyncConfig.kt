package com.gunyoung.infokt.common.config

import com.gunyoung.infokt.common.async.AsyncExceptionHandler
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

@Configuration
@EnableAsync
class AsyncConfig(
    val asyncExceptionHandler: AsyncExceptionHandler
) : AsyncConfigurer {

    companion object {
        const val CORE_POOL_SIZE = 10
        const val MAX_POOL_SIZE = 20
        const val QUEUE_CAPACITY = 1000
    }

    override fun getAsyncExecutor(): Executor? = ThreadPoolTaskExecutor().apply {
        corePoolSize = CORE_POOL_SIZE
        maxPoolSize = MAX_POOL_SIZE
        setQueueCapacity(QUEUE_CAPACITY)
        setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        setWaitForTasksToCompleteOnShutdown(true)
        setThreadNamePrefix("info-asyncExecutor-")
        initialize()
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? = asyncExceptionHandler
}