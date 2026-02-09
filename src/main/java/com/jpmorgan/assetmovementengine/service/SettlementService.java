package com.jpmorgan.assetmovementengine.service;

import com.jpmorgan.assetmovementengine.domain.Party;
import com.jpmorgan.assetmovementengine.domain.SettlementMessage;
import com.jpmorgan.assetmovementengine.domain.SsiRecord;
import com.jpmorgan.assetmovementengine.domain.TradeRequest;
import com.jpmorgan.assetmovementengine.exception.SettlementNotFoundException;
import com.jpmorgan.assetmovementengine.exception.SsiNotFoundException;
import com.jpmorgan.assetmovementengine.repository.SettlementMessageRepository;
import com.jpmorgan.assetmovementengine.repository.SsiRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SettlementService {

    private final SsiRepository ssiRepository;
    private final SettlementMessageRepository messageRepository;
    private final SupportingInformationFormatter formatter;

    public SettlementService(
            SsiRepository ssiRepository,
            SettlementMessageRepository messageRepository,
            SupportingInformationFormatter formatter
    ) {
        this.ssiRepository = ssiRepository;
        this.messageRepository = messageRepository;
        this.formatter = formatter;
    }

    public SettlementMessage create(TradeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        SsiRecord ssi = ssiRepository.findByCode(request.ssiCode())
                .orElseThrow(() -> new SsiNotFoundException(request.ssiCode()));

        SettlementMessage message = new SettlementMessage(
                request.tradeId(),
                UUID.randomUUID(),
                request.amount(),
                request.valueDate(),
                request.currency(),
                new Party(ssi.payerAccountNumber(), ssi.payerBank()),
                new Party(ssi.receiverAccountNumber(), ssi.receiverBank()),
                formatter.format(ssi.supportingInformation())
        );

        return messageRepository.saveIfAbsent(message);
    }

    public SettlementMessage getByTradeId(String tradeId) {
        return messageRepository.findByTradeId(tradeId)
                .orElseThrow(() -> new SettlementNotFoundException(tradeId));
    }
}
