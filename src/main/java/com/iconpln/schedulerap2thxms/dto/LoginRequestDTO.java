package com.iconpln.schedulerap2thxms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LoginRequestDTO implements Serializable {
    private String username;
    private String password;
}
