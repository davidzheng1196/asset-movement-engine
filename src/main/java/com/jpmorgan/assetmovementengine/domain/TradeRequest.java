package com.jpmorgan.assetmovementengine.domain;
import java.math.BigDecimal;

public record TradeRequest(
        String tradeId,
        String ssiCode,
        BigDecimal amount,
        String currency,
        String valueDate
) {}
