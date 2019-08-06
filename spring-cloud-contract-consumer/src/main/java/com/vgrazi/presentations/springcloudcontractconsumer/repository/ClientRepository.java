package com.vgrazi.presentations.springcloudcontractconsumer.repository;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ClientRepository {
    @Value("${default-credit-limit}")
    private double defaultCreditLimit;
    @Value("${default-cash-on-deposit}")
    private double defaultCashOnDeposit;
    @Getter final Map<Integer, Client> clients = new HashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    public Client createClient(String name, String taxId) {
        int clientId = atomicInteger.incrementAndGet();
        Client client = new Client(clientId, name, taxId, defaultCreditLimit, defaultCashOnDeposit);
        clients.put(clientId, client);
        return client;
    }
}
