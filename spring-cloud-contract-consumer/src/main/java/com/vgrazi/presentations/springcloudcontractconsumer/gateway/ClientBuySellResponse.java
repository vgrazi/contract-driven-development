package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Returns the client id, stock purchased, and number of shares
 */
@AllArgsConstructor
public class ClientBuySellResponse {
    @Getter private int clientId;
    @Getter private Stock stock;
    @Getter private int shares;
}
