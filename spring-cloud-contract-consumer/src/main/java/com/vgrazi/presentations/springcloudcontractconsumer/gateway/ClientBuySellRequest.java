package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Encapsulates a request to buy (shares > 0) or sell (shares < 0) a stock
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientBuySellRequest {
    private int clientId;
    private Stock stock;
    private int shares;
}
