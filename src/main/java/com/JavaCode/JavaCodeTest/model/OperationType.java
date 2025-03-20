package com.JavaCode.JavaCodeTest.model;

import lombok.Getter;

@Getter
public enum OperationType {
    Deposit("deposit"), Withdraw("withdraw");
    private final String abbreviation;
    OperationType(String operationType) {
        abbreviation = operationType;
    }
}
