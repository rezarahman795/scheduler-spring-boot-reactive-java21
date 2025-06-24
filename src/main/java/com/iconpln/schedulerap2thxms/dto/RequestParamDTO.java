package com.iconpln.schedulerap2thxms.dto;

import com.iconpln.schedulerap2thxms.respone.ResponeContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data

public class RequestParamDTO implements Serializable {
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
    private String jenisData;

    public RequestParamDTO(ResponeContent content) {
        this.eeGroupCode = content.getEeGroupCode();
        this.eeGroupName = content.getEeGroupName();
        this.eeSubGroupCode = content.getEeSubGroupCode();
        this.eeSubGroupName = content.getEeSubGroupName();
        this.email = content.getEmail();
        this.flagged = content.getFlagged();
        this.namaPegawai = content.getNamaPegawai();
        this.nip = content.getNip();
        this.personalAreaCode = content.getPersonalAreaCode();
        this.personalAreaName = content.getPersonalAreaName();
        this.personalSubAreaCode = content.getPersonalSubAreaCode();
        this.personalSubAreaName = content.getPersonalSubAreaName();
        this.plnGroupCode = content.getPlnGroupCode();
        this.plnGroupName = content.getPlnGroupName();
        this.jenisData = "PEGAWAI NON AKTIF";

    }
}
