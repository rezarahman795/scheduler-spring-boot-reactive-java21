package com.iconpln.schedulerap2thxms.proxy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iconpln.schedulerap2thxms.credential.ApiCredentials;
import com.iconpln.schedulerap2thxms.dto.LoginRequestDTO;
import com.iconpln.schedulerap2thxms.respone.ApiRespone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class LoginProxy {

    private static final String LOGIN_URL = "/auth/login";
    private static final Duration TIMEOUT_DURATION = Duration.ofSeconds(10);

    private final WebClient webClient;
    private final HXMSproxy hxmsProxy;
    private final ObjectMapper objectMapper;
    private final Gson gson;

    private String accessToken;

    @Autowired
    public LoginProxy(@Lazy WebClient webClient, HXMSproxy hxmsProxy, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.hxmsProxy = hxmsProxy;
        this.objectMapper = objectMapper;
        this.gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();
    }

    public Mono<ApiRespone> execute() {
        log.info("STAND BY FOR EXECUTE DATA");
        return login()
                .flatMap(token -> {
                    if (token != null) {
                        log.info("ACCESS_TOKEN Found, Called Method pushOnHXMS()");
                        return hxmsProxy.pushOn(token);
                    } else {
                        log.warn("ACCESS_TOKEN NULL, Break To pushOnHXMS()");
                        return Mono.empty();
                    }
                });
    }

    // Method untuk login dan mengambil JWT Token
    public Mono<String> login() {
        log.info("->>>> Push Login for get access token");
        ApiCredentials apiCredentials = hxmsProxy.getUrlHXMS();
        LoginRequestDTO requestBody = new LoginRequestDTO(apiCredentials.getUsername(), apiCredentials.getPassword());

        return webClient.post()
                .uri(LOGIN_URL)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(TIMEOUT_DURATION)
                .flatMap(this::parseAndExtractToken)
                .onErrorResume(e -> {
                    log.error("Error during login: {}", e.getMessage(), e);
                    return Mono.error(new RuntimeException("Login failed, IP Server OFF", e));
                });
    }

    // Method untuk mem-parsing dan mengekstrak JWT token dari response
    private Mono<String> parseAndExtractToken(String responseBody) {
        try {
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

            return Optional.ofNullable((String) responseMap.get("jwtToken"))
                    .map(token -> {
                        log.info("->>>> Received JWT Token: {}", token);
                        this.accessToken = token;  // Simpan token
                        return Mono.just(token);
                    })
                    .orElseGet(() -> {
                        log.error("JWT Token not found in response");
                        return Mono.error(new RuntimeException("JWT Token not found in response"));
                    });
        } catch (Exception e) {
            log.error("Error parsing response: {}", e.getMessage(), e);
            return Mono.error(new RuntimeException("Failed to parse response", e));
        }
    }
}



