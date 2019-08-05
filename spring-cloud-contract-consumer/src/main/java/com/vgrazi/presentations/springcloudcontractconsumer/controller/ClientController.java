package com.vgrazi.presentations.springcloudcontractconsumer.controller;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Position;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.ClientBuySellRequest;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.ClientBuySellResponse;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.ClientHoldingResponse;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.ClientHoldingsRequest;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.ClientRepository;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PortfolioRepository;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PricingRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ClientController {

    private final PortfolioRepository portfolioRepository;
    private final ClientRepository clientRepository;
    private final PricingRepository pricingRepository;

    private final RestTemplate restTemplate;

    public ClientController(RestTemplate restTemplate, PortfolioRepository portfolioRepository, ClientRepository clientRepository, PricingRepository pricingRepository) {
        this.restTemplate = restTemplate;
        this.portfolioRepository = portfolioRepository;
        this.clientRepository = clientRepository;
        this.pricingRepository = pricingRepository;
    }

    @PostMapping(value = "/holdings", consumes = APPLICATION_JSON_VALUE)
    public ClientHoldingResponse getAllHoldings(@RequestBody ClientHoldingsRequest request) {

        int clientId = request.getClientId();
        Client client = portfolioRepository.getClient(clientId);
        if(client == null) {
            throw new IllegalArgumentException("Unknown client ID " + clientId);
        }
        List<Position> positions = portfolioRepository.getHoldings(client);
        return new ClientHoldingResponse(positions);
    }

    @PostMapping(value = "/buy-sell", consumes = APPLICATION_JSON_VALUE)
    public ClientBuySellResponse placeBuySellOrder(@RequestBody ClientBuySellRequest request) {

        int clientId = request.getClientId();
        Stock stock = request.getStock();
        int shares = request.getShares();
        Client client  = portfolioRepository.getClient(clientId);
        if(client == null) {
            throw new IllegalArgumentException("Unknown client id " +clientId);
        }
        if(shares < 0) {
            portfolioRepository.placeSellOrder(client, stock, -shares);
        }
        else {
            boolean ok = portfolioRepository.getAvailableFunds(client) >= pricingRepository.getPrice(stock) * shares;

            if (ok) {
                portfolioRepository.placeBuyOrder(client, stock, shares);
            } else {
                // todo: add call to provider to increase credit line
                // need to request an increase in creditLine
                throw new IllegalArgumentException("Insufficient credit - apply for increase");
            }
        }
        return new ClientBuySellResponse(client, stock, shares);
    }

    @GetMapping("/create-client")
    public Client createClient(@RequestParam String name,
                               @RequestParam(name = "tax-id") String taxId){
        // todo: move to client provider
        return clientRepository.createClient(name, taxId);
    }
}
