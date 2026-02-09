package com.jpmorgan.assetmovementengine.controller;

import com.jpmorgan.assetmovementengine.domain.TradeRequest;
import com.jpmorgan.assetmovementengine.dto.CreateSettlementRequest;
import com.jpmorgan.assetmovementengine.dto.SettlementResponse;
import com.jpmorgan.assetmovementengine.service.SettlementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SettlementResponse create(@Valid @RequestBody CreateSettlementRequest req) {
        TradeRequest tr = new TradeRequest(
                req.tradeId(),
                req.ssiCode(),
                req.amount(),
                req.currency(),
                req.valueDate()
        );
        return SettlementResponse.from(settlementService.create(tr));
    }

    @GetMapping("/{tradeId}")
    public SettlementResponse getByTradeId(@PathVariable String tradeId) {
        return SettlementResponse.from(settlementService.getByTradeId(tradeId));
    }
}
