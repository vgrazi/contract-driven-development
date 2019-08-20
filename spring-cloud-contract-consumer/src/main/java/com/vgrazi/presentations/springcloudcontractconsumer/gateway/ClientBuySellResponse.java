package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Returns the client id, stock purchased, and number of shares executed
 */
@AllArgsConstructor
@NoArgsConstructor
public class ClientBuySellResponse {
    @Getter private Client client;
    @Getter private Stock stock;
    @Getter private int shares;
    @Getter private double price;
    @Getter private String date;
    @Getter private String denialReason;
}
