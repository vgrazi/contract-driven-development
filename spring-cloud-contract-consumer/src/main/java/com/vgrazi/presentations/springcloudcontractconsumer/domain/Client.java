package com.vgrazi.presentations.springcloudcontractconsumer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * We assume a client has only one portfolio. Client has a cashPosition (cash on reserve + change in portfolio position)
 */
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Getter private int clientId;
    @Getter private List<Position> positions = new CopyOnWriteArrayList<>();
    @Getter private double creditLimit;
    @Getter private double cashOnDeposit;
}
