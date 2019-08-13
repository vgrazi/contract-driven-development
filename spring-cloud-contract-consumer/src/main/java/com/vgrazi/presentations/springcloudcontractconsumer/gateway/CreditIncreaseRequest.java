package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CreditIncreaseRequest {
    @Getter private double currentCreditLine;
    @Getter private double increaseAmount;
    @Getter private int clientId;
}
