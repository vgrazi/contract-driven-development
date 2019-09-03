package com.vgrazi.presentations.springcloudcontractconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Consumer application maintains a list of clients and portfolios, and
 * provides functionality for pricing stock, as well as for buying and selling stocks.
 * (We currently maintains a limited list of about nine stocks)
 *
 * If the client wants to buy a stock, we check their cash position and current debt.
 * If their debt-cash > credit limit, we need to call the provider to ask if they have sufficient credit rating
 */
@SpringBootApplication
public class SpringCloudContractConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudContractConsumerApplication.class, args);
    }
}
