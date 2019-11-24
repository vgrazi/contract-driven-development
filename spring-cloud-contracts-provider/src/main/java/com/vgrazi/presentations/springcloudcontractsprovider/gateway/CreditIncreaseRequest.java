package com.vgrazi.presentations.springcloudcontractsprovider.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditIncreaseRequest {
    private double increaseAmount;
    private int clientId;
    private double currentCreditLine;
    private String date;
}
