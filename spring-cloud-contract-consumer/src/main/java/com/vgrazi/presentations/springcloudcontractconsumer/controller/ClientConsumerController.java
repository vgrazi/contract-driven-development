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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
        Client client = portfolioRepository.getClient(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Unknown client id " + clientId);
        }

        Stock stock = request.getStock();
        int shares = request.getShares();
        double price = pricingRepository.getPrice(stock);
        if (shares < 0) {
            // this is a sell order
            portfolioRepository.placeSellOrder(client, stock, -shares);
        }
        // disregard 0 share purchases, just display the holdings
        else if (shares > 0) {
            // Determine price of the buy order
            double purchasePrice = price * shares;
            // Get the available funds = credit line + profits on existing positions
            double availableFunds = portfolioRepository.getAvailableFunds(client);
            if (availableFunds  >= purchasePrice) {
                // Client has sufficient funds to cover the order. Place the buy order
                portfolioRepository.placeBuyOrder(client, stock, shares);
            } else {
                // Insufficient funds for the order. See if there is available credit line
                double requestedIncrease = purchasePrice - availableFunds;

                // This is the consumer controller, about to make a request to the provider, for a credit line increase

                // request credit increase for the shortage. Server will return with max credit increase up to the requested amount
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(creditIncreaseHost)
                        .setPort(creditIncreasePort)
                        .setPath(creditIncreasePath)
                        .build();
                new DefaultUriBuilderFactory().builder().build();
                CreditIncreaseRequest creditIncreaseRequest = new CreditIncreaseRequest(client.getCreditLimit(), requestedIncrease, client.getClientId(),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE));

                CreditIncreaseResponse creditIncreaseResponse = restTemplate.postForObject(uri, creditIncreaseRequest, CreditIncreaseResponse.class);
                // check if we got our increase. If not, there will be a denial reason
                if(creditIncreaseResponse.getDenialReason() != null) {
                    // return the denial reason. 0 shares were purchased
                    return new ClientBuySellResponse(client, stock, 0, price, creditIncreaseResponse.getDenialReason());
                }

                // get the actual increase. Should be at least the requested amount
                double actualIncrease = creditIncreaseResponse.getIncreaseAmount();
                client.setCreditLimit(client.getCreditLimit() + actualIncrease);
                if (actualIncrease >= requestedIncrease) {
                    portfolioRepository.placeBuyOrder(client, stock, shares);
                } else {
                    // we didn't get enough credit increase. Notify the client
                    return new ClientBuySellResponse(client, stock, 0, price, "Insufficient credit");
                }
            }
        }

        return new ClientBuySellResponse(client, stock, shares, price, null);
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

    @PostConstruct
    private void postContruct() {
        createClient("Victor", "123-45-6789");
        createClient("John", "246-80-135");
    }
}
