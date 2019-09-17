package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class ClientHoldingsRequest {
    private int clientId;
}
