package com.vgrazi.presentations.springcloudcontractconsumer.repository;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ClientRepository {
    @Getter final Map<Integer, Client> clients = new HashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    public Client createClient(String name, String taxId) {
        int clientId = atomicInteger.incrementAndGet();
        Client client = new Client(clientId, name, taxId, 100_000, 100_000);
        clients.put(clientId, client);
        return client;
    }
}
