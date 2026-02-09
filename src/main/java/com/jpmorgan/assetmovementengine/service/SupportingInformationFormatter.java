package com.jpmorgan.assetmovementengine.service;

import org.springframework.stereotype.Component;

@Component
public class SupportingInformationFormatter {

    /**
     * Converts SSI supporting info like "BNF:FFC-4697132" into "/BNF/FFC-4697132".
     * If the input doesn't contain ':', returns it as-is.
     */
    public String format(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }

        int idx = raw.indexOf(':');
        if (idx < 0) {
            return raw;
        }

        String left = raw.substring(0, idx).trim();
        String right = raw.substring(idx + 1).trim();

        if (left.isEmpty() && right.isEmpty()) {
            return "";
        }
        if (left.isEmpty()) {
            // weird input like ":value" — keep original to avoid inventing meaning
            return raw;
        }
        if (right.isEmpty()) {
            return "/" + left;
        }
        return "/" + left + "/" + right;
    }
}
