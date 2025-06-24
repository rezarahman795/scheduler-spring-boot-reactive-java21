package com.iconpln.schedulerap2thxms.respone;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class ApiRespone implements Serializable {
    private int status;
    private boolean success;
    private ResponeData data;
}
