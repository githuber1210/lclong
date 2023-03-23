package com.example.common.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class testTask {
    private Logger LOGGER = LoggerFactory.getLogger(testTask.class);

    /**
     * Seconds Minutes Hours  Month Week [Year]
     */
    @Scheduled(cron = "0 0 12 * * ?  ")
    private void test() {
        LOGGER.info("现在是中午的12点!!!!!!!!!!");
    }
}