package com.vgrazi.presentations.springcloudcontractconsumer.repository;

import com.vgrazi.presentations.springcloudcontractconsumer.domain.Client;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Position;
import com.vgrazi.presentations.springcloudcontractconsumer.domain.Stock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PortfolioRepository {

    final ClientRepository clientRepository;
    final PricingRepository pricingRepository;

    public PortfolioRepository(ClientRepository clientRepository, PricingRepository pricingRepository) {
        this.clientRepository = clientRepository;
        this.pricingRepository = pricingRepository;
    }

    public List<Position> getHoldings(Client client) {
        List<Position> positions = client.getPositions();
        return positions;
    }

    /**
     * Checks the client's available funds, which equals their cash, plus gains, + credit limit - credit already used
     */
    public double getAvailableFunds(Client client) {
        // get cash value
        double cash = getCashReserve(client);
        // get current portfolio value
        double current = evaluatePortfolio(client);
        // Credit line used is the original portfolio value
        double used = evaluateOriginalPortfolio(client);
        // if they made a profit, we add that to their available credit
        double profit = current - used;
        // get their total credit limit
        double creditLimit = getCreditLimit(client);
        // if((available:cash+profit-used) + limit >= (purchase:stock.price*quantity)) then authorize the buy
        return (cash + profit) + (creditLimit - used);
    }

    /**
     * Verifies the client has sufficient funds + credit. If not, throws an exception
     * buy= shares>0; sell=shares<0. These are long only. We can't sell something we don't already own
     */
    public void placeBuyOrder(Client client, Stock stock, int shares) {
        // check if has available credit
        double price = pricingRepository.getPrice(stock);
        double availableFunds = getAvailableFunds(client);
        double purchase = shares * price;
        if (availableFunds >= purchase) {
            // if sufficient funds, place order
            client.getPositions().add(new Position(stock, shares, price));
        } else {
            throw new IllegalArgumentException("client " + client + " has insufficient funds " + availableFunds + ". Does not cover purchase " + purchase);
        }
    }

    /**
     * Verifies the client has enough shares, and sells them, starting with the oldest holdings (FIFO)
     * If not enough shares, throwa an exception
     */
    public void placeSellOrder(Client client, Stock stock, int shares) {
        // first, confirm the total position > shares
        double totalShares = getTotalShares(client, stock);
        if (totalShares < shares) {
            throw new IllegalArgumentException("Trying to sell " + shares + " shares of " + stock + ". Only " + totalShares + " are available");
        }
        List<Position> positions = getPositions(stock, client);
        for (int i = 0; i < positions.size() && shares > 0; i++) {
            Position position = positions.get(i);
            if (position.getShares() <= shares) {
                shares = position.getShares() - shares;
                removeFirstPosition(position, client);
            } else {
                position.setShares(position.getShares() - shares);
                break;
            }
        }
    }

    /**
     * Removes the oldest holding matching this stock from the client's portfolio
     */
    private void removeFirstPosition(Position position, Client client) {
        List<Position> positions = client.getPositions();
        positions.remove(position);
    }

    public double getTotalValue(Client client, Stock stock) {
        List<Position> positions = getPositions(stock, client);
        return positions.stream().mapToDouble(this::getCurrentValue).sum();
    }

    private double getCurrentValue(Position position) {
        return position.getShares() * pricingRepository.getPrice(position.getStock());
    }

    private double getTotalShares(Client client, Stock stock) {
        List<Position> positions = getPositions(stock, client);
        return positions.stream().mapToInt(Position::getShares).sum();
    }

    private void addPosition(Position position, int clientId) {
        Client client = getClient(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client " + clientId + " does not exist");
        }
        List<Position> positions = client.getPositions();
        positions.add(position);
    }

    private List<Position> getPositions(Stock stock, Client client) {
        List<Position> positions = client.getPositions();
        Position proxyPosition = new Position(stock, 0, 0);
        return positions.stream().filter(position -> position.equals(proxyPosition)).collect(Collectors.toList());
    }

    private double evaluatePortfolio(Client client) {
        double sum = 0;
        List<Position> positions = client.getPositions();
        if (positions != null) {
            sum = positions.stream().mapToDouble(position -> position.getShares() * pricingRepository.getPrice(position.getStock())).sum();
        }
        return sum;
    }

    private double evaluateOriginalPortfolio(Client client) {
        double sum = 0;
        if (client != null) {
            List<Position> positions = client.getPositions();
            if (positions != null) {
                sum = positions.stream().mapToDouble(position -> position.getShares() * position.getPurchasePrice()).sum();
            }
        }
        return sum;
    }

    private double getCashReserve(Client client) {
        double cash = client.getCashOnDeposit();
        return cash;
    }

    private double getCreditLimit(Client client) {
        double limit = client.getCreditLimit();
        return limit;
    }

    public Client getClient(int clientId) {
        Client client = clientRepository.clients.get(clientId);
        return client;
    }
}
