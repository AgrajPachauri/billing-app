package com.app.billing.dto;

import lombok.Data;
import java.util.List;

@Data
public class DeleteRequest {
    private List<Long> ids;
}