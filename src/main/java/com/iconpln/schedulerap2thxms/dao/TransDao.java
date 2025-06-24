package com.iconpln.schedulerap2thxms.dao;

import com.iconpln.schedulerap2thxms.dto.RequestParamDTO;
import com.iconpln.schedulerap2thxms.model.ResultModel;
import reactor.core.publisher.Mono;

public interface TransDao {
    public Mono<ResultModel> setDataKirimHXMS(RequestParamDTO data);
}
