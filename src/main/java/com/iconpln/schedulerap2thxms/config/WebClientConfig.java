package com.iconpln.schedulerap2thxms.config;

import com.iconpln.schedulerap2thxms.proxy.HXMSproxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Lazy HXMSproxy hxmSproxy) {
        return WebClient.builder()
                .baseUrl(hxmSproxy.getUrlHXMS().getBaseUrl())
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

}
