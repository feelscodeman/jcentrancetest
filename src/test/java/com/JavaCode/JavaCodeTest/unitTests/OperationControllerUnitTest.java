package com.JavaCode.JavaCodeTest.unitTests;


import com.JavaCode.JavaCodeTest.controller.OperationController;
import com.JavaCode.JavaCodeTest.dto.TransferMoneyRequest;
import com.JavaCode.JavaCodeTest.exceptions.NoSuchWalletException;
import com.JavaCode.JavaCodeTest.exceptions.NotEnoughBalanceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import com.JavaCode.JavaCodeTest.repository.WalletRepository;
import com.JavaCode.JavaCodeTest.service.TransferMoneyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.UUID;

    @WebMvcTest(OperationController.class)
    public class OperationControllerUnitTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private TransferMoneyService service;

        @MockitoBean
        private WalletRepository repo;

        @BeforeEach
        void setUp() {
            ReflectionTestUtils.setField(service, "walletRepository", repo);
        }

    @Test
    public void transferMoneyHappyFlow() throws Exception {

        doNothing().when(service).transferMoney(any(), any(), any());

        TransferMoneyRequest request = new TransferMoneyRequest();
        request.setOperationType("deposit");
        request.setAmount(BigDecimal.valueOf(300L));
        request.setWalletId(UUID.randomUUID());

        mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void findingWalletHappyFlow() throws Exception {

        UUID mockId = UUID.randomUUID();

        given(service.getBalance(mockId))
                .willReturn(BigDecimal.ONE);

        mockMvc.perform(get("/api/v1/wallets/" + mockId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(BigDecimal.ONE.toString()));
    }

    @Test
    public void findingWalletSadFlow() throws Exception {

        UUID mockId = UUID.randomUUID();

        when(service.getBalance(mockId))
                .thenThrow(new NoSuchWalletException("No wallet with this id: " + mockId));

        mockMvc.perform(get("/api/v1/wallets/" + mockId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("No wallet with this id: " + mockId));
    }

    @Test
    public void transferringMoneySadFlowNoSuchWallet() throws Exception {
            UUID mockId = UUID.randomUUID();

        doThrow(new NoSuchWalletException("No wallet with this id: " + mockId))
                .when(service).transferMoney(any(), any(), any());

        TransferMoneyRequest request = new TransferMoneyRequest();
        request.setOperationType("deposit");
        request.setAmount(BigDecimal.valueOf(300L));
        request.setWalletId(mockId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(request)))
                        .andExpect(status().is4xxClientError())
                .andExpect(content().string("No wallet with this id: " + mockId));
    }

    @Test
    public void transferringMoneySadFlowNotEnoughMoney() throws Exception {

            doThrow(new NotEnoughBalanceException("Not enough balance. Please, top it up"))
                    .when(service).transferMoney(any(), any(), any());

        TransferMoneyRequest request = new TransferMoneyRequest();
        request.setOperationType("deposit");
        request.setAmount(BigDecimal.valueOf(300L));
        request.setWalletId(UUID.randomUUID());

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Not enough balance. Please, top it up"));
    }

    @Test
    public void transferringMoneyNonExistentOperation() throws Exception {
            doThrow(new UnsupportedOperationException("Operation does not exist"))
                    .when(service).transferMoney(any(), any(), any());

        TransferMoneyRequest request = new TransferMoneyRequest();
        request.setOperationType("deposit");
        request.setAmount(BigDecimal.valueOf(300L));
        request.setWalletId(UUID.randomUUID());

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Operation does not exist"));
    }

    private static String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
