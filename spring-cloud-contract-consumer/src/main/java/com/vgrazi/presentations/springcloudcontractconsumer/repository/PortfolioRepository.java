package com.vgrazi.presentations.springcloudcontractconsumer.repository;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Position;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PortfolioRepository {
//    private List<Position> holdings = Arrays.asList(
//            new Position(new Stock("AAPL", "NAS", 0), 1200), // "NASDAQ",
//            new Position(new Stock("GOOG", "NAS", 0), 1400), // "NASDAQ",
//            new Position(new Stock("MSFT", "NAS", 0), 1000), // "NASDAQ",
//            new Position(new Stock("FB", "NAS", 0), 1000), // "NASDAQ",
//            new Position(new Stock("XOM", "NYSE", 0), 1100), // "NYSE",
//            new Position(new Stock("PFE", "NYSE", 0), 1210), // "NYSE",
//            new Position(new Stock("WMT", "NYSE", 0), 1450), // "NYSE",
//            new Position(new Stock("BARC", "LSE", 0), 1080), // "LSE",
//            new Position(new Stock("BP", "LSE", 0), 1080) // "LSE",
//    );


    public PortfolioRepository() {
    }

    private final Map<Integer, Client> clients = new HashMap<>();

    public List<Position> getHoldings(int clientId) {
        Client client = getClient(clientId);
        if (client != null) {
            List<Position> positions = client.getPositions();
            return positions;
        }
        throw new IllegalArgumentException(clientId + " is not a known client");
    }

    /**
     * Checks the client's available funds, which equals their cash, plus gains, + credit limit
     */
    public double getAvailableFunds(int clientId) {
        if(clients.get(clientId) == null) {
            throw new IllegalArgumentException("Unknown client " + clientId);
        }
        // get cash value
        double cash = getCashReserve(clientId);
        // get current portfolio value
        double current = evaluatePortfolio(clientId);
        // get original portfolio value
        double original = evaluateOriginalPortfolio(clientId);
        // get credit limit
        double creditLimit = getCreditLimit(clientId);
        // if((reserve:cash+current-original) + limit >= (purchase:stock.price*quantity)) return ok, else return not ok
        return cash + current - original + creditLimit;
    }

    /**
     * If this is a sell order, verifies the client has enough shares, and sells them, starting with the oldest holdings (FIFO)
     * if this is a buy order, verifies the client has sufficient funds + credit. If not, throws an exception
     * buy= shares>0; sell=shares<0. These are long only. We can't sell something we don't already own
     */
    public void placeBuySellOrder(int clientId, Stock stock, int shares) {
        Client client = getClient(clientId);
        if(client == null) {
            throw new IllegalArgumentException("Client " + clientId + " does not exist");
        }
        if(shares < 0) {
            // this is a sell order
            shares = -shares;
            // first, confirm the total position > shares
            double totalShares = getTotalShares(clientId, stock);
            if (totalShares < shares) {
                throw new IllegalArgumentException("Trying to sell " + shares + " shares of " + stock + ". Only " + totalShares + " are available");
            }
            List<Position> positions = getPositions(stock, clientId);
            for (int i = 0; i < positions.size() && shares > 0; i++) {
                Position position = positions.get(i);
                if (position.getShares() <= shares) {
                    removeFirstPosition(position, clientId);
                    shares = position.getShares() - shares;
                }
            }
        }
        else {
            // this is a buy order
            // check if has available credit
            double price = stock.getPrice();
            double availableFunds = getAvailableFunds(clientId);
            double purchase = shares * price;
            if(availableFunds >= purchase) {
                // if sufficient funds, place order
                client.getPositions().add(new Position(stock, shares, price));
            }
            else {
                throw new IllegalArgumentException("client id " + clientId + " has available funds " + availableFunds + ". Does not cover purchase " + purchase);
            }
        }
    }

    /**
     * Removes the oldest holding matching this stock from the client's portfolio
     */
    private void removeFirstPosition(Position position, int clientId) {
        Client client = getClient(clientId);
        if(client == null) {
            throw new IllegalArgumentException("Client " + clientId + " does not exist");
        }
        List<Position> positions = client.getPositions();
        positions.remove(position);

    }

    public double getTotalValue(int clientId, Stock stock) {
        Client client = getClient(clientId);
        if(client == null) {
            throw new IllegalArgumentException("Client " + clientId + " does not exist");
        }
        List<Position> positions = getPositions(stock, clientId);
        return positions.stream().mapToDouble(Position::getCurrentValue).sum();
    }

    public double getTotalShares(int clientId, Stock stock) {
        Client client = getClient(clientId);
        if(client == null) {
            throw new IllegalArgumentException("Client " + clientId + " does not exist");
        }
        List<Position> positions = getPositions(stock, clientId);
        return positions.stream().mapToInt(Position::getShares).sum();
    }

    private void addPosition(Position position, int clientId) {
        Client client = getClient(clientId);
        if(client == null) {
            throw new IllegalArgumentException("Client " + clientId + " does not exist");
        }
        List<Position> positions = client.getPositions();
        positions.add(position);
    }

    private List<Position> getPositions(Stock stock, int clientId) {
        Client client = getClient(clientId);
        if(client == null) {
            throw new IllegalArgumentException("Client " + clientId + " does not exist");
        }
        List<Position> positions = client.getPositions();
        Position proxyPosition = new Position(stock, 0, 0);
        return positions.stream().filter(position -> position.equals(proxyPosition)).collect(Collectors.toList());
    }

    private double evaluatePortfolio(int clientId) {
        double sum = 0;
        Client client = getClient(clientId);
        if (client != null) {
            List<Position> positions = client.getPositions();
            if (positions != null) {
                sum = positions.stream().mapToDouble(position -> position.getShares() * position.getStock().getPrice()).sum();
            }
        }
        return sum;
    }

    private double evaluateOriginalPortfolio(int clientId) {
        double sum = 0;
        Client client = getClient(clientId);
        if (client != null) {
            List<Position> positions = client.getPositions();
            if (positions != null) {
                sum = positions.stream().mapToDouble(position -> position.getShares() * position.getPurchasePrice()).sum();
            }
        }
        return sum;
    }

    private double getCashReserve(int clientId) {
        double cash = 0;
        Client client = getClient(clientId);
        if (client != null) {
            cash = client.getCashOnDeposit();
        }
        return cash;
    }

    private double getCreditLimit(int clientId) {
        double limit = 0;
        Client client = getClient(clientId);
        if (client != null) {
            limit = client.getCreditLimit();
        }
        return limit;
    }

    public Client getClient(int clientId) {
        Client client = clients.get(clientId);
        return client;
    }
}
