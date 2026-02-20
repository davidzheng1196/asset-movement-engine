package com.jpmorgan.assetmovementengine.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "settlement_message")
public class SettlementMessageEntity {

    @Id
    @Column(name = "trade_id", nullable = false, length = 64)
    private String tradeId;

    @Column(name = "message_id", nullable = false, length = 36)
    private String messageId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "value_date", nullable = false, length = 32)
    private String valueDate;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "payer_account_number", nullable = false, length = 64)
    private String payerAccountNumber;

    @Column(name = "payer_bank", nullable = false, length = 64)
    private String payerBank;

    @Column(name = "receiver_account_number", nullable = false, length = 64)
    private String receiverAccountNumber;

    @Column(name = "receiver_bank", nullable = false, length = 64)
    private String receiverBank;

    @Column(name = "supporting_information", nullable = false, length = 256)
    private String supportingInformation;

    protected SettlementMessageEntity() {
        // JPA requires a no-arg constructor
    }

    public SettlementMessageEntity(String tradeId,
                                   UUID messageId,
                                   BigDecimal amount,
                                   String valueDate,
                                   String currency,
                                   String payerAccountNumber,
                                   String payerBank,
                                   String receiverAccountNumber,
                                   String receiverBank,
                                   String supportingInformation) {
        this.tradeId = tradeId;
        this.messageId = messageId.toString();
        this.amount = amount;
        this.valueDate = valueDate;
        this.currency = currency;
        this.payerAccountNumber = payerAccountNumber;
        this.payerBank = payerBank;
        this.receiverAccountNumber = receiverAccountNumber;
        this.receiverBank = receiverBank;
        this.supportingInformation = supportingInformation == null ? "" : supportingInformation;
    }

    public String getTradeId() {
        return tradeId;
    }

    public UUID getMessageId() {
        return UUID.fromString(messageId);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getValueDate() {
        return valueDate;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public String getPayerBank() {
        return payerBank;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public String getReceiverBank() {
        return receiverBank;
    }

    public String getSupportingInformation() {
        return supportingInformation;
    }
}