package com.vgrazi.presentations.springcloudcontractconsumer.domain;

import com.vgrazi.presentations.springcloudcontractconsumer.repository.PricingRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;


@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class Stock {
    @EqualsAndHashCode.Exclude
    @Autowired
    private PricingRepository pricingRepository;

    @Getter
    private final String symbol;
    @Getter
    private final String exchange;

    public double getPrice() {
        return pricingRepository.getPrice(this);
    }
}
