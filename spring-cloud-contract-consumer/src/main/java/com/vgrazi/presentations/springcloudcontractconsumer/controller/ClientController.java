package com.vgrazi.presentations.springcloudcontractconsumer.controller;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Position;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import com.vgrazi.presentations.springcloudcontractconsumer.gateway.*;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.ClientRepository;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PortfolioRepository;
import com.vgrazi.presentations.springcloudcontractconsumer.repository.PricingRepository;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ClientController {

    private final PortfolioRepository portfolioRepository;
    private final ClientRepository clientRepository;
    private final PricingRepository pricingRepository;
    @Value("${min-credit-increase-request}")
    private double minCreditIncreaseRequest;
    @Value("${credit-increase-host}")
    private String creditIncreaseHost = "localhost";
    @Value("${credit-increase-port}")
    private int creditIncreasePort = 8081;
    @Value("${credit-increase-path}")
    private String creditIncreasePath = "/request-credit-increase";


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
        if (client == null) {
            throw new IllegalArgumentException("Unknown client ID " + clientId);
        }
        List<Position> positions = portfolioRepository.getHoldings(client);
        return new ClientHoldingResponse(positions);
    }

    @PostMapping(value = "/buy-sell", consumes = APPLICATION_JSON_VALUE)
    public ClientBuySellResponse placeBuySellOrder(@RequestBody ClientBuySellRequest request) throws URISyntaxException {

        int clientId = request.getClientId();
        Stock stock = request.getStock();
        int shares = request.getShares();
        Client client = portfolioRepository.getClient(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Unknown client id " + clientId);
        }
        if (shares < 0) {
            portfolioRepository.placeSellOrder(client, stock, -shares);
        }
        // disregard 0 share purchases, just display the holdings
        else if (shares > 0) {
            double surplus = portfolioRepository.getAvailableFunds(client) - pricingRepository.getPrice(stock) * shares;

            if (surplus >= 0) {
                portfolioRepository.placeBuyOrder(client, stock, shares);
            } else {
                // todo: add call to provider to increase credit line
                // request credit increase for double the shortage. Server will return with max credit increase up to the requested amount
                double creditIncrease = -surplus * 2;
                if (creditIncrease < minCreditIncreaseRequest) {
                    creditIncrease = minCreditIncreaseRequest;
                }

                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(creditIncreaseHost)
                        .setPort(creditIncreasePort)
                        .setPath(creditIncreasePath)
                        .build();
                new DefaultUriBuilderFactory().builder().build();
                CreditIncreaseRequest creditIncreaseRequest = new CreditIncreaseRequest(client.getCreditLimit(), creditIncrease);

                Double increase = restTemplate.postForObject(uri, creditIncreaseRequest, Double.class);
                client.setCreditLimit(client.getCreditLimit() + increase);
                // need to request an increase in creditLine
                if (increase < -surplus){
                    throw new IllegalArgumentException("Insufficient credit - apply for increase");
                }
                portfolioRepository.placeBuyOrder(client, stock, shares);
            }
        }
        return new ClientBuySellResponse(client, stock, shares);
    }

    @GetMapping("/create-client")
    public Client createClient(@RequestParam String name,
                               @RequestParam(name = "tax-id") String taxId) {
        // todo: move to client provider
        return clientRepository.createClient(name, taxId);
    }
}
