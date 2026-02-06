package com.jpmorgan.assetmovementengine.repository.inmemory;

import com.jpmorgan.assetmovementengine.domain.SettlementMessage;
import com.jpmorgan.assetmovementengine.exception.DuplicateTradeIdException;
import com.jpmorgan.assetmovementengine.repository.SettlementMessageRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySettlementMessageRepository implements SettlementMessageRepository {
    private final ConcurrentHashMap<String, SettlementMessage> store = new ConcurrentHashMap<>();

    @Override
    public Optional<SettlementMessage> findByTradeId(String tradeId){
        return Optional.ofNullable(store.get(tradeId));
    }

    @Override
    public SettlementMessage saveIfAbsent(SettlementMessage message) {
        SettlementMessage existing = store.putIfAbsent(message.tradeId(), message);
        if (existing != null) {
            throw new DuplicateTradeIdException(message.tradeId());
        }
        return message;
    }
}
