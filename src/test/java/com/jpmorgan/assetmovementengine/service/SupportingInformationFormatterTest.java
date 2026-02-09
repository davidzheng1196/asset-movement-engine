package com.jpmorgan.assetmovementengine.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupportingInformationFormatterTest {

    private final SupportingInformationFormatter formatter = new SupportingInformationFormatter();

    @Test
    void formatsKeyValueToSlashFormat() {
        assertEquals("/BNF/FFC-4697132", formatter.format("BNF:FFC-4697132"));
    }

    @Test
    void returnsRawIfNoColon() {
        assertEquals("HELLO", formatter.format("HELLO"));
    }

    @Test
    void blanksBecomeEmptyString() {
        assertEquals("", formatter.format("   "));
        assertEquals("", formatter.format(null));
    }
}
