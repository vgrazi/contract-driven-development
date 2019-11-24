package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreditIncreaseRequest {
    private double currentCreditLine;
    private double increaseAmount;
    private int clientId;
    private long date;
}
