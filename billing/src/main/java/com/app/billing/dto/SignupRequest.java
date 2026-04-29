package com.app.billing.dto;

import lombok.Data;

@Data
public class SignupRequest {

    private String name;
    private String email;
    private String password;

    private String businessName;
    private String ownerName;
    private String address;
    private String contact;
    private String gstNumber;
    private String logoPath;
    private String qrPath;
}