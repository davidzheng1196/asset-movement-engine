package com.jpmorgan.assetmovementengine.repository.inmemory;

import com.jpmorgan.assetmovementengine.domain.SsiRecord;
import com.jpmorgan.assetmovementengine.repository.SsiRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class InMemorySsiRepository implements SsiRepository {

    private static final Map<String, SsiRecord> SSI_BY_CODE = Map.of(
            "DBS_OCBC_1", new SsiRecord("DBS_OCBC_1", "05461368", "DBSSGB2LXXX", "438421", "OCBCSGSGXXX", "BNF:PAY CLIENT"),
            "OCBC_DBS_1", new SsiRecord("OCBC_DBS_1", "438421", "OCBCSGSGXXX", "05461368", "DBSSGB2LXXX", "BNF:FFC-4697132"),
            "OCBC_DBS_2", new SsiRecord("OCBC_DBS_2", "438421", "OCBCSGSGXXX", "05461369", "DBSSSGSGXXX", "BNF:FFC-482315"),
            "DBS_SCB",    new SsiRecord("DBS_SCB",    "185586",  "DBSSSGSGXXX", "1868422", "SCBLAU2SXXX", "RFB:Test payment"),
            "CITI_GS",    new SsiRecord("CITI_GS",    "00454983","CITIGB2LXXX", "48486414","GSCMUS33XXX", "")
    );

    @Override
    public Optional<SsiRecord> findByCode(String ssiCode) {
        return Optional.ofNullable(SSI_BY_CODE.get(ssiCode));
    }
}
