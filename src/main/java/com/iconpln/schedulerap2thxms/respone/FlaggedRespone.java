package com.iconpln.schedulerap2thxms.respone;

import lombok.Data;

import java.io.Serializable;

@Data
public class FlaggedRespone implements Serializable {
    private int status;
    private boolean success;
    private String data;
}
