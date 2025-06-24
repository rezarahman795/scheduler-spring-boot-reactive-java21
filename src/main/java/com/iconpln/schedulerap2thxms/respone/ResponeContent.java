package com.iconpln.schedulerap2thxms.respone;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class ResponeContent implements Serializable {
    private String eeGroupCode;
    private String eeGroupName;
    private String eeSubGroupCode;
    private String eeSubGroupName;
    private String email;
    private String flagged;
    private String namaPegawai;
    private String nip;
    private String personalAreaCode;
    private String personalAreaName;
    private String personalSubAreaCode;
    private String personalSubAreaName;
    private String plnGroupCode;
    private String plnGroupName;
}
