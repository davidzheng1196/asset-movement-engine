package com.jpmorgan.assetmovementengine.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ssi")
public class SsiEntity {

    @Id
    @Column(name = "ssi_code", nullable = false, length = 64)
    private String ssiCode;

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

    protected SsiEntity() {
        // JPA requires a no-arg constructor
    }

    public SsiEntity(String ssiCode,
                     String payerAccountNumber,
                     String payerBank,
                     String receiverAccountNumber,
                     String receiverBank,
                     String supportingInformation) {
        this.ssiCode = ssiCode;
        this.payerAccountNumber = payerAccountNumber;
        this.payerBank = payerBank;
        this.receiverAccountNumber = receiverAccountNumber;
        this.receiverBank = receiverBank;
        this.supportingInformation = supportingInformation == null ? "" : supportingInformation;
    }

    public String getSsiCode() {
        return ssiCode;
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