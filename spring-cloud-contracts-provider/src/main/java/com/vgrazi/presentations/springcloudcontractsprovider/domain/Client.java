package com.vgrazi.presentations.springcloudcontractsprovider.domain;

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
    @Getter @Setter
    private double creditLimit;
}
