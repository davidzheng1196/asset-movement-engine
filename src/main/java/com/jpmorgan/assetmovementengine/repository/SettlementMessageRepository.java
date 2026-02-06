package com.jpmorgan.assetmovementengine.repository;

import com.jpmorgan.assetmovementengine.domain.SettlementMessage;
import java.util.Optional;

public interface SettlementMessageRepository {
    Optional<SettlementMessage> findByTradeId(String tradeId);

    SettlementMessage saveIfAbsent(SettlementMessage message);
}
