package com.vgrazi.presentations.springcloudcontractconsumer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Getter private double creditLimit;
    @Getter private double cashOnDeposit;
    @Getter private final List<Position> positions = new CopyOnWriteArrayList<>();
}
