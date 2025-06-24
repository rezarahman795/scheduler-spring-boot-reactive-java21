package com.iconpln.schedulerap2thxms.dao;

import com.iconpln.schedulerap2thxms.dto.RequestParamDTO;
import com.iconpln.schedulerap2thxms.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

@Slf4j
@Repository
public class TransDaoImpl implements TransDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Mono<ResultModel> setDataKirimHXMS(RequestParamDTO data) {
        return Mono.fromCallable(() -> {
                    ResultModel result = new ResultModel();
                    return jdbcTemplate.execute((Connection con) -> {
                        CallableStatement call = null;
                        try {
                            String sql = "{? = call ganti_data_base_anda(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                            call = con.prepareCall(sql);
                            call.registerOutParameter(1, Types.INTEGER);
                            call.setString(2, data.getJenisData());
                            call.setString(3, data.getNip());
                            call.setString(4, data.getNamaPegawai());
                            call.setString(5, data.getEmail());
                            call.setString(6, data.getEeGroupCode());
                            call.setString(7, data.getEeGroupName());
                            call.setString(8, data.getEeSubGroupCode());
                            call.setString(9, data.getEeSubGroupName());
                            call.setString(10, data.getPersonalAreaCode());
                            call.setString(11, data.getPersonalAreaName());
                            call.setString(12, data.getPersonalSubAreaCode());
                            call.setString(13, data.getPersonalSubAreaName());
                            call.setString(14, data.getPlnGroupCode());
                            call.setString(15, data.getPlnGroupName());
                            call.registerOutParameter(16, Types.VARCHAR);

                            call.execute();

                            result.setOut_return(call.getInt(1));
                            result.setOut_message(call.getString(16));

                            call.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            result.setOut_return(-1);
                            result.setOut_message(ex.getLocalizedMessage());
                        }
                        return result;
                    });
                }).subscribeOn(Schedulers.boundedElastic()) // Jalankan di thread pool terpisah untuk blocking I/O
                .onErrorResume(ex -> {
                    ResultModel errorResult = new ResultModel();
                    errorResult.setOut_return(-1);
                    errorResult.setOut_message(ex.getMessage());
                    return Mono.just(errorResult);
                });
    }
}
