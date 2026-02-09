package com.jpmorgan.assetmovementengine.service;

import com.jpmorgan.assetmovementengine.domain.SettlementMessage;
import com.jpmorgan.assetmovementengine.domain.SsiRecord;
import com.jpmorgan.assetmovementengine.domain.TradeRequest;
import com.jpmorgan.assetmovementengine.exception.SsiNotFoundException;
import com.jpmorgan.assetmovementengine.repository.SettlementMessageRepository;
import com.jpmorgan.assetmovementengine.repository.SsiRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettlementServiceTest {

    @Test
    void createEnrichesUsingSsiAndPersists() {
        SsiRepository ssiRepository = mock(SsiRepository.class);
        SettlementMessageRepository messageRepository = mock(SettlementMessageRepository.class);
        SupportingInformationFormatter formatter = new SupportingInformationFormatter();

        when(ssiRepository.findByCode("OCBC_DBS_1"))
                .thenReturn(Optional.of(new SsiRecord(
                        "OCBC_DBS_1",
                        "438421",
                        "OCBCSGSGXXX",
                        "05461368",
                        "DBSSGB2LXXX",
                        "BNF:FFC-4697132"
                )));

        // capture what we save
        ArgumentCaptor<SettlementMessage> captor = ArgumentCaptor.forClass(SettlementMessage.class);
        when(messageRepository.saveIfAbsent(any())).thenAnswer(inv -> inv.getArgument(0));

        SettlementService service = new SettlementService(ssiRepository, messageRepository, formatter);

        TradeRequest req = new TradeRequest(
                "16846548",
                "OCBC_DBS_1",
                new BigDecimal("12894.65"),
                "USD",
                "20022020"
        );

        SettlementMessage out = service.create(req);

        assertEquals("16846548", out.tradeId());
        assertNotNull(out.messageId());
        assertEquals(new BigDecimal("12894.65"), out.amount());
        assertEquals("USD", out.currency());
        assertEquals("20022020", out.valueDate());
        assertEquals("438421", out.payerParty().accountNumber());
        assertEquals("OCBCSGSGXXX", out.payerParty().bankCode());
        assertEquals("05461368", out.receiverParty().accountNumber());
        assertEquals("DBSSGB2LXXX", out.receiverParty().bankCode());
        assertEquals("/BNF/FFC-4697132", out.supportingInformation());

        verify(messageRepository).saveIfAbsent(captor.capture());
        assertEquals(out, captor.getValue());
    }

    @Test
    void createThrowsWhenSsiMissing() {
        SsiRepository ssiRepository = mock(SsiRepository.class);
        SettlementMessageRepository messageRepository = mock(SettlementMessageRepository.class);

        when(ssiRepository.findByCode("ocbc_dbs_999")).thenReturn(Optional.empty());

        SettlementService service = new SettlementService(
                ssiRepository,
                messageRepository,
                new SupportingInformationFormatter()
        );

        TradeRequest req = new TradeRequest(
                "1",
                "ocbc_dbs_999",
                new BigDecimal("1.00"),
                "USD",
                "20022020"
        );

        SsiNotFoundException ex = assertThrows(SsiNotFoundException.class, () -> service.create(req));
        assertEquals("SSI code ocbc_dbs_999 not found", ex.getMessage());
        verify(messageRepository, never()).saveIfAbsent(any());
    }
}
