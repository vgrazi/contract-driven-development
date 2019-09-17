package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreditIncreaseRequest {
    private double currentCreditLine;
    private double increaseAmount;
    private int clientId;
    private String date;
}
