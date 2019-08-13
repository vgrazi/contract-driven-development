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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ClientConsumerController {

    private PortfolioRepository portfolioRepository;
    private ClientRepository clientRepository;
    private PricingRepository pricingRepository;
    private RestTemplate restTemplate;

    private String creditIncreaseHost;
    private int creditIncreasePort;
    private String creditIncreasePath;

    public ClientConsumerController(RestTemplate restTemplate, PortfolioRepository portfolioRepository,
                                    ClientRepository clientRepository, PricingRepository pricingRepository,
                                    @Value("${credit-increase-host}") String creditIncreaseHost,
                                    @Value("${credit-increase-port}") int creditIncreasePort,
                                    @Value("${credit-increase-path}") String creditIncreasePath
    ) {
        this.restTemplate = restTemplate;
        this.portfolioRepository = portfolioRepository;
        this.clientRepository = clientRepository;
        this.pricingRepository = pricingRepository;
        this.creditIncreaseHost = creditIncreaseHost;
        this.creditIncreasePort = creditIncreasePort;
        this.creditIncreasePath = creditIncreasePath;
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
                double creditIncrease = -surplus;

                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(creditIncreaseHost)
                        .setPort(creditIncreasePort)
                        .setPath(creditIncreasePath)
                        .build();
                new DefaultUriBuilderFactory().builder().build();
                CreditIncreaseRequest creditIncreaseRequest = new CreditIncreaseRequest(client.getCreditLimit(), creditIncrease, clientId);

                CreditIncreaseResponse response = restTemplate.postForObject(uri, creditIncreaseRequest, CreditIncreaseResponse.class);
                double increase = response.getIncrease();
                client.setCreditLimit(client.getCreditLimit() + increase);
                surplus = portfolioRepository.getAvailableFunds(client) - pricingRepository.getPrice(stock) * shares;
                // need to request an increase in creditLine
                if (increase < -surplus){
                    throw new IllegalArgumentException("Insufficient credit - apply for increase");
                }
                portfolioRepository.placeBuyOrder(client, stock, shares);
            }
        }
        return new ClientBuySellResponse(client, stock, shares);
    }

    @PostMapping(value = "/ping", consumes = APPLICATION_JSON_VALUE)
    public String ping() throws IOException {
        return "{" +
                "\"ok\" = \"ok\"" +
                "}";
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

    @GetMapping("/create-client")
    public Client createClient(@RequestParam String name,
                               @RequestParam(name = "tax-id") String taxId) {
        // todo: move to client provider
        return clientRepository.createClient(name, taxId);
    }
}
