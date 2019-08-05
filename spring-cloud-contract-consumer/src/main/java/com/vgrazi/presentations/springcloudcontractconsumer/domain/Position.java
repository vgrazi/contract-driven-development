package com.vgrazi.presentations.springcloudcontractconsumer.domain;

import lombok.*;

/**
 * Positions are a little complicated. If you buy more shares of an existing stock, that's a new position, even though the
 * stock already exists. This is because we need to track the original purchase price of all stocks
 * However if you are selling stock, then we look for the first stock in the portfolio that matches, and sell from that
 * If more left over, we sell from that, etc.
 * If we are sellinjg more than we bought, the transaction is void
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Position {
    @Getter private Stock stock;
    @EqualsAndHashCode.Exclude @Getter @Setter private int shares;
    @EqualsAndHashCode.Exclude @Getter private double purchasePrice;

    public double getCurrentValue() {
        return stock.getPrice() * shares;
    }
}
