package com.jpmorgan.assetmovementengine.repository;

import com.jpmorgan.assetmovementengine.domain.SsiRecord;
import java.util.Optional;

public interface SsiRepository {
    Optional<SsiRecord> findByCode(String ssiCode);
}
