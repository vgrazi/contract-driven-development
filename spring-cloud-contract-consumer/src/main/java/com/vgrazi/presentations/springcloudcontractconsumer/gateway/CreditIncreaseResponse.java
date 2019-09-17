package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditIncreaseResponse {
    private int clientId;
    private double increaseAmount;
    private String denialReason;
}
