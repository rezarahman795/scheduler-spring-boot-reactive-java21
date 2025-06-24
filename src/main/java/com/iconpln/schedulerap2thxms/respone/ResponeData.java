package com.iconpln.schedulerap2thxms.respone;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ResponeData implements Serializable {
    private List<ResponeContent> content;
}
