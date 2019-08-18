package com.vgrazi.presentations.springcloudcontractsprovider.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CreditIncreaseRequest {
    @Getter private double currentCreditLine;
    @Getter private double increaseAmount;
    @Getter private int clientId;
}
