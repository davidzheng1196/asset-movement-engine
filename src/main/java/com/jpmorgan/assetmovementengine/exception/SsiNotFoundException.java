package com.jpmorgan.assetmovementengine.exception;

public class SsiNotFoundException extends RuntimeException {
    public SsiNotFoundException(String ssiCode) {
        super("SSI code " + ssiCode + " not found");
    }
}
