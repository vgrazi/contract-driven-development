package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import lombok.*;

/**
 * Returns the client id, stock purchased, and number of shares executed
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientBuySellResponse {
    private Client client;
    private Stock stock;
    private int shares;
    private double price;
    private String denialReason;
}
