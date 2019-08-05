package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Encapsulates a request to buy (shares > 0) or sell (shares < 0) a stock
 */
@AllArgsConstructor
public class ClientBuySellRequest {
    @Getter private int clientId;
    @Getter private Stock stock;
    @Getter private int shares;
}
