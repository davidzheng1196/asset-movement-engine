package com.jpmorgan.assetmovementengine.exception;

public class SettlementNotFoundException extends RuntimeException {
    public SettlementNotFoundException(String tradeId) {
        super("tradeId " + tradeId + " not found");
    }
}
