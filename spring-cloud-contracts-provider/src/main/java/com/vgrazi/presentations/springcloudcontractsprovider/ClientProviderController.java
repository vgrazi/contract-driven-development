package com.vgrazi.presentations.springcloudcontractsprovider;

import com.vgrazi.presentations.springcloudcontractsprovider.gateway.CreditIncreaseRequest;
import com.vgrazi.presentations.springcloudcontractsprovider.gateway.CreditIncreaseResponse;
import com.vgrazi.presentations.springcloudcontractsprovider.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientProviderController {

    /**
     * If the user has available credit ($1M),
     * If the request is not a multiple of 10K rounds up to the next 10K
     * Then adds their current credit line to the request
     * If that is too much, returns the maximum credit line they can have
     * If they are already at the maximum, throws an exception
     */
    @Value("${rounding}")
    private int rounding;
    @Value("${max-credit-line}")
    private double maxCreditline;

    @PostMapping("/request-credit-increase")
    public CreditIncreaseResponse handleCreditIncreaseRequest(@RequestBody CreditIncreaseRequest creditIncreaseRequest) {
        double currentCreditLine = creditIncreaseRequest.getCurrentCreditLine();
        double increaseRounded = Utils.round(creditIncreaseRequest.getIncreaseAmount(), rounding);
        double totalCreditLine = currentCreditLine + increaseRounded;

        double increase;

        if (totalCreditLine > maxCreditline) {
            // request is for more than the max. Bring them to the max
            increase = maxCreditline - currentCreditLine;
            if (increase <= 0) {
                throw new IllegalArgumentException("Credit line has reached its max");
            }
        } else {
            increase = increaseRounded;
        }
        return new CreditIncreaseResponse(creditIncreaseRequest.getClientId(), increase);
    }

}
