package com.iconpln.schedulerap2thxms.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class ResultModel implements Serializable {
    private int out_return;
    private String out_message;
}
