package com.jpmorgan.assetmovementengine.repository.jpa.repo;

import com.jpmorgan.assetmovementengine.repository.jpa.SsiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SsiJpaRepository extends JpaRepository<SsiEntity, String> {
    // findById(ssiCode) is already provided by JpaRepository
}