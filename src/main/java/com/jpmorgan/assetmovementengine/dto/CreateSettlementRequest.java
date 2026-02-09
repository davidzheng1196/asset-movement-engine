package com.jpmorgan.assetmovementengine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateSettlementRequest(
        @NotBlank @Size(max = 64) String tradeId,
        @NotBlank @Size(max = 64) String ssiCode,
        @NotNull @Positive BigDecimal amount,
        @NotBlank @Size(max = 3) String currency,
        @NotBlank @Size(max = 32) String valueDate
) {}
