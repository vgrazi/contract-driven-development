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
    @Getter private int clientId;
    private String name;
    @Getter private String taxId;
    @Getter @Setter private double creditLimit;
    @Getter private double cashOnDeposit;
    @Getter private final List<Position> positions = new CopyOnWriteArrayList<>();
}
