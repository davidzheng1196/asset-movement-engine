package com.jpmorgan.assetmovementengine.dto;

import com.jpmorgan.assetmovementengine.domain.SettlementMessage;

import java.math.BigDecimal;
import java.util.UUID;

public record SettlementResponse(
        String tradeId,
        UUID messageId,
        BigDecimal amount,
        String valueDate,
        String currency,
        PartyResponse payerParty,
        PartyResponse receiverParty,
        String supportingInformation
) {
    public static SettlementResponse from(SettlementMessage m) {
        return new SettlementResponse(
                m.tradeId(),
                m.messageId(),
                m.amount(),
                m.valueDate(),
                m.currency(),
                new PartyResponse(m.payerParty().accountNumber(), m.payerParty().bankCode()),
                new PartyResponse(m.receiverParty().accountNumber(), m.receiverParty().bankCode()),
                m.supportingInformation()
        );
    }

    public record PartyResponse(String accountNumber, String bankCode) {}
}
