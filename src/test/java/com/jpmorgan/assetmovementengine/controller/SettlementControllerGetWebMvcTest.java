package com.jpmorgan.assetmovementengine.controller;

import com.jpmorgan.assetmovementengine.domain.Party;
import com.jpmorgan.assetmovementengine.domain.SettlementMessage;
import com.jpmorgan.assetmovementengine.exception.GlobalExceptionHandler;
import com.jpmorgan.assetmovementengine.exception.SettlementNotFoundException;
import com.jpmorgan.assetmovementengine.service.SettlementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SettlementController.class)
@Import(GlobalExceptionHandler.class)
class SettlementControllerGetWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettlementService settlementService;

    @Test
    void getByTradeId_returns200_andBody() throws Exception {
        SettlementMessage msg = new SettlementMessage(
                "16846548",
                UUID.fromString("d82e7a94-217a-4ea1-84bc-f0d173a85a2d"),
                new BigDecimal("12894.65"),
                "20022020",
                "USD",
                new Party("438421", "OCBCSGSGXXX"),
                new Party("05461368", "DBSSGB2LXXX"),
                "/BNF/FFC-4697132"
        );

        when(settlementService.getByTradeId("16846548")).thenReturn(msg);

        mockMvc.perform(get("/api/settlements/16846548"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tradeId").value("16846548"))
                .andExpect(jsonPath("$.messageId").value("d82e7a94-217a-4ea1-84bc-f0d173a85a2d"));
    }

    @Test
    void getByTradeId_returns404_whenNotFound() throws Exception {
        when(settlementService.getByTradeId("999"))
                .thenThrow(new SettlementNotFoundException("999"));

        mockMvc.perform(get("/api/settlements/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("tradeId 999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
