package com.JavaCode.JavaCodeTest.controller;

import com.JavaCode.JavaCodeTest.dto.TransferMoneyRequest;
import com.JavaCode.JavaCodeTest.service.TransferMoneyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class OperationController {
private final TransferMoneyService moneyService;

@PostMapping("/wallet")
public void transferMoney(
        @RequestBody TransferMoneyRequest request
        ) {
moneyService.transferMoney(request.getOperationType(), request.getWalletId(), request.getAmount());
}

@GetMapping("/wallets/{walletId}")
public BigDecimal getBalanceByWalletId(@PathVariable UUID walletId) {
    return moneyService.getBalance(walletId);
}
}
