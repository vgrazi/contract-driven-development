package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CreditIncreaseRequest {
    @Getter private double currentCreditLine;
    @Getter private double increaseAmount;
}
