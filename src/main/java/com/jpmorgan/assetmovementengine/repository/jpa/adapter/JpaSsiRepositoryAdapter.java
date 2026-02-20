package com.jpmorgan.assetmovementengine.repository.jpa.adapter;

import com.jpmorgan.assetmovementengine.domain.SsiRecord;
import com.jpmorgan.assetmovementengine.repository.SsiRepository;
import com.jpmorgan.assetmovementengine.repository.jpa.SsiEntity;
import com.jpmorgan.assetmovementengine.repository.jpa.repo.SsiJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaSsiRepositoryAdapter implements SsiRepository {

    private final SsiJpaRepository ssiJpaRepository;

    public JpaSsiRepositoryAdapter(SsiJpaRepository ssiJpaRepository) {
        this.ssiJpaRepository = ssiJpaRepository;
    }

    @Override
    public Optional<SsiRecord> findByCode(String ssiCode) {
        return ssiJpaRepository.findById(ssiCode).map(JpaSsiRepositoryAdapter::toDomain);
    }

    private static SsiRecord toDomain(SsiEntity e) {
        return new SsiRecord(
                e.getSsiCode(),
                e.getPayerAccountNumber(),
                e.getPayerBank(),
                e.getReceiverAccountNumber(),
                e.getReceiverBank(),
                e.getSupportingInformation()
        );
    }
}