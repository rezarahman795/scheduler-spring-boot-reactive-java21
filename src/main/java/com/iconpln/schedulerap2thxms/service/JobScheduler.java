package com.iconpln.schedulerap2thxms.service;

import com.iconpln.schedulerap2thxms.proxy.HXMSproxy;
import com.iconpln.schedulerap2thxms.proxy.LoginProxy;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Slf4j
@Component
@NoArgsConstructor
public class JobScheduler {

    @Autowired
    private LoginProxy loginProxy;

    // Job dijalankan setiap 10 detik
    @Scheduled(fixedRate = 10000, initialDelay = 1000)
    public synchronized void doSendJob() {
        log.info("Starting job ...");

        loginProxy.execute()
                .subscribe();
    }
}

