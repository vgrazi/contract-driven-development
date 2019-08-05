package com.vgrazi.presentations.springcloudcontractconsumer.domain;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class StockPrice {
    private final Stock stock;
    @Getter @Setter @EqualsAndHashCode.Exclude
    private double price;
}
