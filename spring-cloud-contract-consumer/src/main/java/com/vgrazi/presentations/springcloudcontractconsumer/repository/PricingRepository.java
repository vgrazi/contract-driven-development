package com.vgrazi.presentations.springcloudcontractconsumer.repository;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.StockPrice;
import com.vgrazi.presentations.springcloudcontractconsumer.util.RandomWalk;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@EnableScheduling
public class PricingRepository {
    final ApplicationContext applicationContext;
    private final List<StockPrice> stockPrices = new CopyOnWriteArrayList<>();

    public PricingRepository(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        addStock("AAPL", "NASD", 207.74);
        addStock("GOOG", "NASD", 118.29);
        addStock("MSFT", "NASD", 141.34);
        addStock("FB", "NASD", 199.75);
        addStock("XOM", "NYSE", 74.81);
        addStock("PFE", "NYSE", 43.09);
        addStock("WMT", "NYSE", 113.02);
        addStock("BARC", "LSE", 160.08);
        addStock("BP", "LSE", 523.00);

    }

    private void addStock(String symbol, String exchange, double price) {
        Stock stock = (Stock) applicationContext.getBean("stock");
        stock.setSymbol(symbol);
        stock.setExchange(exchange);
        stockPrices.add(new StockPrice(stock, price));
    }

    @Scheduled(fixedRate = 10_000)
    private synchronized void recalculatePrices() {
        stockPrices.forEach(stockPrice -> {
            double newPrice = stockPrice.getPrice() * RandomWalk.monteCarlo();
//            System.out.println("Changing price of " + stockPrice + newPrice);
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
