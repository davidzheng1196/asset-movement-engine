package com.jpmorgan.assetmovementengine.repository.jpa.adapter;

import com.jpmorgan.assetmovementengine.domain.Party;
import com.jpmorgan.assetmovementengine.domain.SettlementMessage;
import com.jpmorgan.assetmovementengine.exception.DuplicateTradeIdException;
import com.jpmorgan.assetmovementengine.repository.SettlementMessageRepository;
import com.jpmorgan.assetmovementengine.repository.jpa.SettlementMessageEntity;
import com.jpmorgan.assetmovementengine.repository.jpa.repo.SettlementMessageJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaSettlementMessageRepositoryAdapter implements SettlementMessageRepository {

    private final SettlementMessageJpaRepository jpaRepository;

    public JpaSettlementMessageRepositoryAdapter(SettlementMessageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<SettlementMessage> findByTradeId(String tradeId) {
        return jpaRepository.findById(tradeId).map(JpaSettlementMessageRepositoryAdapter::toDomain);
    }

    @Override
    public SettlementMessage saveIfAbsent(SettlementMessage message) {
        // Quick existence check (nice error message, no DB exception in normal path)
        if (jpaRepository.existsById(message.tradeId())) {
            throw new DuplicateTradeIdException(message.tradeId());
        }

        try {
            SettlementMessageEntity saved = jpaRepository.save(toEntity(message));
            return toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            // Handles race conditions if two threads insert same tradeId concurrently
            throw new DuplicateTradeIdException(message.tradeId());
        }
    }

    private static SettlementMessageEntity toEntity(SettlementMessage m) {
        return new SettlementMessageEntity(
                m.tradeId(),
                m.messageId(),
                m.amount(),
                m.valueDate(),
                m.currency(),
                m.payerParty().accountNumber(),
                m.payerParty().bankCode(),
                m.receiverParty().accountNumber(),
                m.receiverParty().bankCode(),
                m.supportingInformation()
        );
    }

    private static SettlementMessage toDomain(SettlementMessageEntity e) {
        return new SettlementMessage(
                e.getTradeId(),
                e.getMessageId(),
                e.getAmount(),
                e.getValueDate(),
                e.getCurrency(),
                new Party(e.getPayerAccountNumber(), e.getPayerBank()),
                new Party(e.getReceiverAccountNumber(), e.getReceiverBank()),
                e.getSupportingInformation()
        );
    }
}