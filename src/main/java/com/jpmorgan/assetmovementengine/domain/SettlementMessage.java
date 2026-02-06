package com.jpmorgan.assetmovementengine.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record SettlementMessage(
        String tradeId,
        UUID messageId,
        BigDecimal amount,
        String valueDate,
        String currency,
        Party payerParty,
        Party receiverParty,
        String supportInformation
) {}
