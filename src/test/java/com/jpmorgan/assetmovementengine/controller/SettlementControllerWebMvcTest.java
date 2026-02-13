package com.jpmorgan.assetmovementengine.controller;

import com.jpmorgan.assetmovementengine.domain.Party;
import com.jpmorgan.assetmovementengine.domain.SettlementMessage;
import com.jpmorgan.assetmovementengine.exception.DuplicateTradeIdException;
import com.jpmorgan.assetmovementengine.exception.GlobalExceptionHandler;
import com.jpmorgan.assetmovementengine.exception.SsiNotFoundException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SettlementController.class)
@Import(GlobalExceptionHandler.class)
class SettlementControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettlementService settlementService;

    @Test
    void postCreate_returns201_andResponseBody() throws Exception {
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

        when(settlementService.create(any())).thenReturn(msg);

        String body = """
                {
                  "tradeId":"16846548",
                  "ssiCode":"OCBC_DBS_1",
                  "amount":12894.65,
                  "currency":"USD",
                  "valueDate":"20022020"
                }
                """;

        mockMvc.perform(post("/api/settlements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tradeId").value("16846548"))
                .andExpect(jsonPath("$.messageId").value("d82e7a94-217a-4ea1-84bc-f0d173a85a2d"))
                .andExpect(jsonPath("$.payerParty.accountNumber").value("438421"))
                .andExpect(jsonPath("$.receiverParty.bankCode").value("DBSSGB2LXXX"))
                .andExpect(jsonPath("$.supportingInformation").value("/BNF/FFC-4697132"));
    }

    @Test
    void postCreate_returns404_whenSsiMissing() throws Exception {
        when(settlementService.create(any()))
                .thenThrow(new SsiNotFoundException("ocbc_dbs_999"));

        String body = """
                {
                  "tradeId":"1",
                  "ssiCode":"ocbc_dbs_999",
                  "amount":1,
                  "currency":"USD",
                  "valueDate":"20022020"
                }
                """;

        mockMvc.perform(post("/api/settlements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SSI code ocbc_dbs_999 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void postCreate_returns409_whenDuplicateTradeId() throws Exception {
        when(settlementService.create(any()))
                .thenThrow(new DuplicateTradeIdException("16846548"));

        String body = """
                {
                  "tradeId":"16846548",
                  "ssiCode":"OCBC_DBS_1",
                  "amount":12894.65,
                  "currency":"USD",
                  "valueDate":"20022020"
                }
                """;

        mockMvc.perform(post("/api/settlements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("tradeId 16846548 already exists"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void postCreate_returns400_whenValidationFails() throws Exception {
        // tradeId blank, amount negative, currency too long
        String body = """
                {
                  "tradeId":"",
                  "ssiCode":"OCBC_DBS_1",
                  "amount":-1,
                  "currency":"USDD",
                  "valueDate":"20022020"
                }
                """;

        mockMvc.perform(post("/api/settlements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
