package com.vgrazi.presentations.springcloudcontractconsumer.domain;

import lombok.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * We assume a client has only one portfolio. Client has a cashPosition (cash on reserve + change in portfolio position)
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Client {
    @Getter
    private int clientId;
    private String name;
    @Getter
    private String taxId;
    @Getter
    @Setter
    private double creditLimit;
    @Getter
    private double cashOnDeposit;
    @Getter
    private final List<Position> positions = new CopyOnWriteArrayList<>();

    public void addPosition(Position position) {
        Position newHolding = positions.stream().filter(h -> h.getStock().equals(position.getStock())).findFirst()
                .map(holding -> {
                    positions.remove(holding);
                    // set the average shares and prices
                    int existingShares = holding.getShares();
                    double existingPrice = holding.getAveragePrice();

                    int purchaseShares = position.getShares();
                    double averagePrice = position.getAveragePrice();

                    int totalShares = existingShares + purchaseShares;
                    double newAveragePrice = (existingPrice * existingShares + averagePrice * purchaseShares) / (existingShares + purchaseShares);

                    holding.setShares(totalShares);
                    holding.setAveragePrice(newAveragePrice);
                    return holding;

                })
                .orElse(position);
        positions.add(newHolding);
    }
}
