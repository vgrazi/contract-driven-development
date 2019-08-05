package com.vgrazi.presentations.springcloudcontractconsumer.controller;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Position;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.ClientBuySellRequest;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.ClientBuySellResponse;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.ClientHoldingResponse;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.ClientHoldingsRequest;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PortfolioRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ClientController {

    private final PortfolioRepository portfolioRepository;

    private final RestTemplate restTemplate;

    public ClientController(RestTemplate restTemplate, PortfolioRepository portfolioRepository) {
        this.restTemplate = restTemplate;
        this.portfolioRepository = portfolioRepository;
    }

    @PostMapping(value = "/holdings", consumes = APPLICATION_JSON_VALUE)
    public ClientHoldingResponse getAllHoldings(@RequestBody ClientHoldingsRequest request) {

        int clientId = request.getClientId();
        List<Position> positions = portfolioRepository.getHoldings(clientId);
        return new ClientHoldingResponse(positions);
    }

    @PostMapping(value = "/buy-sell", consumes = APPLICATION_JSON_VALUE)
    public ClientBuySellResponse placeBuySellOrder(@RequestBody ClientBuySellRequest request) {

        int clientId = request.getClientId();
        Stock stock = request.getStock();
        int shares = request.getShares();

        boolean ok = shares < 0 || portfolioRepository.getAvailableFunds(clientId) >=  stock.getPrice() * shares;

        if(ok) {
            portfolioRepository.placeBuySellOrder(clientId, stock, shares);
        }
        else {
            // need to request an increase in creditLine
        }


        return new ClientBuySellResponse(clientId, stock, shares);
    }

    /**
     * Creates a new account for the specified client, and returns the client id
     */
    public int createAccount(String name, String taxId) {
return 0;
    }
}
