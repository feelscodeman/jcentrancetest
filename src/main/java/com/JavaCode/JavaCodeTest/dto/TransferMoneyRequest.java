package com.JavaCode.JavaCodeTest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransferMoneyRequest {
    private UUID walletId;
    private String operationType;
    private BigDecimal amount;
}
