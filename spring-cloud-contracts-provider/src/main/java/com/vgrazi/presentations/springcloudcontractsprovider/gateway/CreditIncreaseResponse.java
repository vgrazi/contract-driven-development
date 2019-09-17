package com.vgrazi.presentations.springcloudcontractsprovider.gateway;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditIncreaseResponse {
    private int clientId;
    private double increaseAmount;
    private String denialReason;

    public CreditIncreaseResponse(int clientId, double increaseAmount) {
        this(clientId, increaseAmount, null);
    }
}
