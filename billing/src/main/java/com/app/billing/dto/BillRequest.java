package com.app.billing.dto;

import lombok.Data;
import java.util.List;

@Data
public class BillRequest {

    private String area;
    private List<Long> customerIds;
    private int month;   // 1–12
    private int year;
}