package com.vgrazi.presentations.springcloudcontractconsumer.gateway;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Data
public class ClientHoldingResponse {
    private List<Position> positions;
}
