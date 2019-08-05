package com.vgrazi.presentations.springcloudcontractsproducer;

import com.vgrazi.presentations.springcloudcontractsproducer.gateway.Holding;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PricingService {

    Map<String, Holding> pricing;


    public PricingService() {
        List<Holding> lastPrices = Arrays.asList(
                new Holding("AAPL", 1200, 207.74), // "NASDAQ",
                new Holding("GOOG", 1400, 118.29), // "NASDAQ",
                new Holding("MSFT", 1000, 141.34), // "NASDAQ",
                new Holding("FB", 1000, 199.75), // "NASDAQ",
                new Holding("XOM", 1100, 74.81), // "NYSE",
                new Holding("PFE", 1210, 43.09), // "NYSE",
                new Holding("WMT", 1450, 113.02), // "NYSE",
                new Holding("BARC", 1080, 160.08), // "LSE",
                new Holding("BP", 1080, 523.00)); // "LSE",

        pricing = lastPrices.stream()
                .collect(Collectors.toConcurrentMap(Holding::getTicker, holding -> holding));
    }

    public void updatePrices() {
        pricing.values().forEach(holding ->{
            double newPrice = holding.getPrice() * RandomWalk.monteCarlo();
            holding.setPrice(newPrice);
        });
    }

}
