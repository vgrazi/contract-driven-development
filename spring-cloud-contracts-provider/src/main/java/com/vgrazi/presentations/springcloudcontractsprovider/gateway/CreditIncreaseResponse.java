package com.vgrazi.presentations.springcloudcontractsprovider.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class CreditIncreaseResponse {
    @Getter @Setter
    private int clientId;
    @Getter @Setter
    private double increaseAmount;

    @Getter @Setter
    private String denialReason;

    @Getter private String date;

    public CreditIncreaseResponse(int clientId, double increaseAmount, String date) {
        this.clientId = clientId;
        this.increaseAmount = increaseAmount;
        this.date = date;
    }
}
