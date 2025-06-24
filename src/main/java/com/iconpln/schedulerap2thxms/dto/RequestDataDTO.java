package com.iconpln.schedulerap2thxms.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class RequestDataDTO implements Serializable {
    private int page;
    private int size;
    private String from;
    private String to;
    private boolean flagged;
}
