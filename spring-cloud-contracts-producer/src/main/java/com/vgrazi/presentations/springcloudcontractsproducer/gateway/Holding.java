package com.vgrazi.presentations.springcloudcontractsproducer.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Holding {
    @Getter private String ticker;
    @Getter private int shares;
    @Getter @Setter private double price;
}
