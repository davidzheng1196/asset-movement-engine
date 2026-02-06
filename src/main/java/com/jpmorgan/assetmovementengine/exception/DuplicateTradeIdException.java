package com.jpmorgan.assetmovementengine.exception;

public class DuplicateTradeIdException extends RuntimeException{
    public DuplicateTradeIdException(String tradeId) {
        super("tradeId " + tradeId + " already exists");
    }
}
