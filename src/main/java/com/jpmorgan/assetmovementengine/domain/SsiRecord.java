package com.jpmorgan.assetmovementengine.domain;

public record SsiRecord(
        String ssiCode,
        String payerAccountNumber,
        String payerBank,
        String receiverAccountNumber,
        String receiverBank,
        String supportingInformation
) {}
