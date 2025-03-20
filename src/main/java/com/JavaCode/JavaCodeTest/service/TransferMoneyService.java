package com.JavaCode.JavaCodeTest.service;

import com.JavaCode.JavaCodeTest.model.OperationType;
import com.JavaCode.JavaCodeTest.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class TransferMoneyService {

    private final WalletRepository walletRepository;

    public void transferMoney(String operation, UUID walletId, BigDecimal amount) {
        OperationType oType = null;
        operation = operation.trim().toLowerCase();

        if (operation.equals(OperationType.Deposit.getAbbreviation()))
            oType = OperationType.Deposit;
        else if (operation.equals(OperationType.Withdraw.getAbbreviation()))
            oType = OperationType.Withdraw;
        else
            throw new UnsupportedOperationException("Operation does not exist");

        if (amount.compareTo(BigDecimal.ZERO) < 0)
            amount = amount.negate();

        switch(oType) {
            case Deposit -> walletRepository.changeBalance(walletId, amount);
            case Withdraw -> walletRepository.changeBalance(walletId, amount.negate());
        }
    }

    public BigDecimal getBalance(UUID walletId) {
        return walletRepository.getBalanceById(walletId);
    }

}
