package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ClientHoldingsRequest {
    @Getter int clientId;
}
