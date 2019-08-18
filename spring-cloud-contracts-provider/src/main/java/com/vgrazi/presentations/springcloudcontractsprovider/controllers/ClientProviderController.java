package com.vgrazi.presentations.springcloudcontractsprovider.controllers;

import com.vgrazi.presentations.springcloudcontractsprovider.domain.Client;
import com.vgrazi.presentations.springcloudcontractsprovider.gateway.CreditIncreaseRequest;
import com.vgrazi.presentations.springcloudcontractsprovider.gateway.CreditIncreaseResponse;
import com.vgrazi.presentations.springcloudcontractsprovider.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ClientProviderController {

    private final Map<Integer, Client> clients = new ConcurrentHashMap<>();

    /**
     * If the user has available credit ($1M),
     * If the request is not a multiple of 10K rounds up to the next 10K
     * Then adds their current credit line to the request
     * If that is too much, returns the maximum credit line they can have
     * If they are already at the maximum, throws an exception
     */
    private int rounding;
    private double defaultCreditLine;
    private double maxCreditline;

    public ClientProviderController(@Value("${rounding}") int rounding, @Value("${default-credit-line}") double defaultCreditLine, @Value("${max-credit-line}") double maxCreditline) {
        this.rounding = rounding;
        this.defaultCreditLine = defaultCreditLine;
        this.maxCreditline = maxCreditline;
    }

    /**
     * If the client has available credit to cover the request, returns that amount.
     * Otherwise returns a response with 0 increase and a denial reason
     */
    @PostMapping("/request-credit-increase")
    public CreditIncreaseResponse handleCreditIncreaseRequest(@RequestBody CreditIncreaseRequest creditIncreaseRequest) {
        double currentCreditLine = getCurrentCreditLine(creditIncreaseRequest);
        double increase = Utils.round(creditIncreaseRequest.getIncreaseAmount(), rounding);
        double totalCreditLine = currentCreditLine + increase;
        updateCreditLine(creditIncreaseRequest.getClientId(), totalCreditLine);


        if (totalCreditLine > maxCreditline) {
            // request is for more than the max. Bring them to the max
            return new CreditIncreaseResponse(creditIncreaseRequest.getClientId(), 0, "Credit line has reached its max. Available: " + (maxCreditline - currentCreditLine), LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        }

        return new CreditIncreaseResponse(creditIncreaseRequest.getClientId(), increase, LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }

    private void updateCreditLine(int clientId, double totalCreditLine) {
        Client client = getClient(clientId);
        client.setCreditLimit(totalCreditLine);
    }

    private double getCurrentCreditLine(@RequestBody CreditIncreaseRequest request) {
        Client client = getClient(request.getClientId());
        if (client == null) {
            client = new Client(request.getClientId(), defaultCreditLine);
            clients.put(request.getClientId(), client);
        }
        return client.getCreditLimit();
    }

    private Client getClient(int clientId) {
        return clients.get(clientId);
    }


}
