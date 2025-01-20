/*
package ru.netology.transferMoney.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.netology.transferMoney.model.TransferRequest;
import ru.netology.transferMoney.service.TransferMoneyService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferMoneyController.class)
public class TransferMoneyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferMoneyService transferMoneyService;

    @Test
    public void testTransferSuccess() throws Exception {
        // Arrange
        String operationId = "12345";
        when(transferMoneyService.transfer(any(TransferRequest.class))).thenReturn(operationId);


        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content("{\"cardFromNumber\":\"1111222233334444\"," +
                                "\"cardFromValidTill\":\"12/24\",\"cardFromCVV\":\"123\"," +
                                "\"cardToNumber\":\"5555666677778888\"," +
                                "\"amount\":{\"value\":\"5000\",\"currency\":\"RUB\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value(operationId));

        verify(transferMoneyService, times(1)).transfer(any(TransferRequest.class));
    }
}
*/