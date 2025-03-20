package com.JavaCode.JavaCodeTest.unitTests;

import com.JavaCode.JavaCodeTest.exceptions.NoSuchWalletException;
import com.JavaCode.JavaCodeTest.model.Wallet;
import com.JavaCode.JavaCodeTest.repository.WalletRepository;
import com.JavaCode.JavaCodeTest.service.TransferMoneyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MoneyTransferServiceUnitTest {

    @Mock
    private WalletRepository walletRepo;

    @InjectMocks
    private TransferMoneyService transferService;

    @Test
    @DisplayName("""
            Test happy flow scenario
            for withdraw balance""")
        public void testHappyFlowWithdraw() {
            Wallet wallet = new Wallet(BigDecimal.valueOf(1000L), 0);
            transferService.transferMoney("withdraw", wallet.getWalletId(), BigDecimal.valueOf(400L));
            verify(walletRepo)
                    .changeBalance(wallet.getWalletId(), BigDecimal.valueOf(-400L));
        }

        @Test
    @DisplayName("""
            Test happy flow scenario
            for deposit balance""")
    public void testHappyFlowDeposit() {
        Wallet wallet = new Wallet(BigDecimal.valueOf(400L), 0);
        transferService.transferMoney("deposit", wallet.getWalletId(), BigDecimal.valueOf(100L));
        verify(walletRepo)
                .changeBalance(wallet.getWalletId(), BigDecimal.valueOf(100L));
        }

        @Test
    @DisplayName("""
            Test sad flow scenario
            for non-existent wallet
            """)
    public void testSadNonExistentWallet() {
        Wallet wallet = new Wallet(BigDecimal.valueOf(300L), 0);
        given(walletRepo.getBalanceById(wallet.getWalletId()))
                .willThrow(NoSuchWalletException.class);

        transferService.transferMoney("deposit", wallet.getWalletId(), BigDecimal.ZERO);

        verify(walletRepo, never())
                .changeAmount(any(), any());
        }

        @Test
        @DisplayName("""
                Test sad flow scenario
                for non-existent operation""")
        public void testSadFlowInvalidOperation() {

            Wallet wallet = new Wallet(BigDecimal.ZERO, 0);

            assertThrows(UnsupportedOperationException.class,
                    () -> transferService.transferMoney("desposit", wallet.getWalletId(), BigDecimal.ZERO));

            verify(walletRepo, never())
                    .changeBalance(any(), any());
        }

        @Test
    @DisplayName("""
            Test sad flow scenario
            for lower balance""")
    public void testSadFlowLowerBalance() {
        doThrow(NoSuchWalletException.class).when(walletRepo).changeBalance(any(), any());
        verify(walletRepo, never())
                .changeAmount(any(), any());
        }
}
