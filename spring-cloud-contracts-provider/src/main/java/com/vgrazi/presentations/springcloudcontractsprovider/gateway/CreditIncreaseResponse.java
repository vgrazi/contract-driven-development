package com.vgrazi.presentations.springcloudcontractsprovider.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class CreditIncreaseResponse {
    @Getter @Setter
    private double clientId;
    @Getter @Setter
    private double increase;
}
