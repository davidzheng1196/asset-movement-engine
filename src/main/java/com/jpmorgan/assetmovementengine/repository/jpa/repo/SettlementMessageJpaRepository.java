package com.jpmorgan.assetmovementengine.repository.jpa.repo;

import com.jpmorgan.assetmovementengine.repository.jpa.SettlementMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementMessageJpaRepository extends JpaRepository<SettlementMessageEntity, String> {
    // findById(tradeId) is already provided by JpaRepository
}