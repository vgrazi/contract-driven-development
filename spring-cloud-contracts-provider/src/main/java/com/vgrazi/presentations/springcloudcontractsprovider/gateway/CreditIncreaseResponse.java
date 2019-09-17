package com.vgrazi.presentations.springcloudcontractsprovider.gateway;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditIncreaseResponse {
    private int clientId;
    private double increaseAmount;
    private String denialReason;
}
