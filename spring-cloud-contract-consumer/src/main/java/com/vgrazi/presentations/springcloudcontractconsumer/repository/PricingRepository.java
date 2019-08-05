package com.vgrazi.presentations.springcloudcontractconsumer.repository;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.StockPrice;
import com.vgrazi.presentations.springcloudcontractconsumer.util.RandomWalk;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@EnableScheduling
public class PricingRepository {
    private final List<StockPrice> stockPrices = new CopyOnWriteArrayList<>(Arrays.asList(
            new StockPrice(new Stock("AAPL", "NASD"), 207.74),
            new StockPrice(new Stock("GOOG", "NASD"), 118.29),
            new StockPrice(new Stock("MSFT", "NASD"), 141.34),
            new StockPrice(new Stock("FB", "NASD"), 199.75),
            new StockPrice(new Stock("XOM", "NYSE"), 74.81),
            new StockPrice(new Stock("PFE", "NYSE"), 43.09),
            new StockPrice(new Stock("WMT", "NYSE"), 113.02),
            new StockPrice(new Stock("BARC", "LSE"), 160.08),
            new StockPrice(new Stock("BP", "LSE"), 523.00)));

    @Scheduled(fixedRate = 10_000)
    private synchronized void recalculatePrices() {
        stockPrices.forEach(stockPrice -> {
            double newPrice = stockPrice.getPrice() * RandomWalk.monteCarlo();
            System.out.println("Changing price of " + stockPrice + newPrice);
            stockPrice.setPrice(newPrice);
        });
    }

    public synchronized double getPrice(Stock stock) {
        int index = stockPrices.indexOf(new StockPrice(stock, 0));
        if (index >= 0) {
            StockPrice stockPrice = stockPrices.get(index);
            return stockPrice.getPrice();
        } else {
            throw new IllegalArgumentException("No pricing info for " + stock);
        }
    }
}
