package com.iconpln.schedulerap2thxms.proxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iconpln.schedulerap2thxms.dao.TransDao;
import com.iconpln.schedulerap2thxms.dto.RequestParamDTO;
import com.iconpln.schedulerap2thxms.respone.ApiRespone;
import com.iconpln.schedulerap2thxms.credential.ApiCredentials;
import com.iconpln.schedulerap2thxms.respone.FlaggedRespone;
import com.iconpln.schedulerap2thxms.respone.ResponeContent;
import com.iconpln.schedulerap2thxms.util.AppUtil;
import com.iconpln.schedulerap2thxms.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@Component
public class HXMSproxy {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PropertiesUtil props;

    @Autowired
    private TransDao transDao;

    private final WebClient webClient;

    @Autowired
    public HXMSproxy(@Lazy WebClient webClient) {
        this.webClient = webClient;
    }

    private final Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();

    public Mono<ApiRespone> pushOn(String accessToken) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/ganti-path-anda")
                        .queryParam("page", props.page)
                        .queryParam("size", props.size)
                        .queryParam("from", props.from)
                        .queryParam("to", LocalDate.now())
                        .queryParam("flagged", props.flagged)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseBody -> processResponse(responseBody, accessToken))  // Method terpisah untuk memproses response
                .onErrorResume(this::handleError);  // Method terpisah untuk handle error
    }

    private Mono<ApiRespone> processResponse(String responseBody,String accessToken ) {
        try {
            ApiRespone responseMap = gson.fromJson(responseBody, ApiRespone.class);
            List<ResponeContent> contents = responseMap.getData().getContent();
            log.info("->>>> Size data contents: {}", contents.size());
            if (contents.isEmpty()) {
                log.info("->>>> Data response HXMS kosong, Data tidak dapat di-flag / di-proses");
                return Mono.just(responseMap);
            }
            return Flux.fromIterable(contents)
                    .flatMap(content -> processContent(content, accessToken))  // Method terpisah untuk proses tiap content
                    .then(Mono.just(responseMap));
        } catch (Exception e) {
            log.error("Error parsing response body: {}", e.getMessage());
            return Mono.error(e);
        }
    }

    private Mono<FlaggedRespone> processContent(ResponeContent content, String accessToken) {
        RequestParamDTO dataParam = new RequestParamDTO(content);
        return transDao.setDataKirimHXMS(dataParam)
                .flatMap(result -> {
                    log.info(">>> Flagging data kirim : {},{}", dataParam.getJenisData(), result.toString());
                    if (result.getOut_return() == 1) {
                        return executeFlagged(dataParam.getNip(), accessToken);
                    }
                    return Mono.empty();
                })
                .doOnError(e -> log.error("Error saving data: {}", e.getMessage()));
    }

    private Mono<ApiRespone> handleError(Throwable e) {
        log.error("ERROR HXMS: {}", e.getMessage());
        return Mono.error(new Exception("Error during processing", e));
    }


    public Mono<FlaggedRespone>executeFlagged(String nip, String access_token){
        log.info("->>>> Execute flagged HXMS");
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/ganti-path-anda")
                        .queryParam("nip", nip)
                        .build())
                .header("Authorization", "Bearer " + access_token)
                .retrieve()
                .bodyToMono(FlaggedRespone.class)
                .onErrorResume(e -> {
                    log.error("ERROR HXMS: {}", e.getMessage());
                    return Mono.error(new Exception("Error during processing", e));
                });
    }

    public ApiCredentials getUrlHXMS() {
        List<Map<String, Object>> lstMap = new ArrayList<>();
        ApiCredentials apiCredentials = new ApiCredentials();
        Connection con = null;
        CallableStatement call = null;
        String sql;
        try {
            con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            con.setAutoCommit(false);

            sql = "{call database_anda(?)}";
            call = con.prepareCall(sql);
            call.registerOutParameter(1, Types.REF_CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            lstMap = AppUtil.convertResultsetToListStr(rs);

            if (!lstMap.isEmpty()) {
                Map<String, Object> credential = lstMap.getFirst();
                apiCredentials.setBaseUrl(Optional.ofNullable(credential.get("url_app")).map(Object::toString).orElse(""));
                apiCredentials.setHost(Optional.ofNullable(credential.get("host_app")).map(Object::toString).orElse(""));
                apiCredentials.setPort(Optional.ofNullable(credential.get("port_app")).map(Object::toString).orElse(""));
                apiCredentials.setUsername(Optional.ofNullable(credential.get("username_app")).map(Object::toString).orElse(""));
                apiCredentials.setPassword(Optional.ofNullable(credential.get("password_app")).map(Object::toString).orElse(""));
            }

            call.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return apiCredentials;
    }

}



