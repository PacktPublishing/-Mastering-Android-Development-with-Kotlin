package com.journaler.execution

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class TaskExecutor private constructor(
        corePoolSize: Int,
        maximumPoolSize: Int,
        workQueue: BlockingQueue<Runnable>?

) : ThreadPoolExecutor(
        corePoolSize,
        maximumPoolSize,
        0L,
        TimeUnit.MILLISECONDS,
        workQueue
) {

    companion object {
        fun getInstance(capacity: Int): TaskExecutor {
            return TaskExecutor(
                    capacity,
                    capacity * 2,
                    LinkedBlockingQueue<Runnable>()
            )
        }
    }

}