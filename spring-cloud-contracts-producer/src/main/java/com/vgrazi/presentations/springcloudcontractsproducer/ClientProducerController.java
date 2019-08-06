package com.vgrazi.presentations.springcloudcontractsproducer;

import com.vgrazi.presentations.springcloudcontractsproducer.gateway.CreditIncreaseRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientProducerController {

    @Value("${max-credit-line}")
    private double maxCreditline;
    @PostMapping("/request-credit-increase")
    public Double handleCreditIncreaseRequest(@RequestBody CreditIncreaseRequest creditIncreaseRequest){
        double totalCreditLine = creditIncreaseRequest.getCurrentCreditLine() + creditIncreaseRequest.getIncreaseAmount();
//        if(true) {
//            return 0.0;
//        }
        if(totalCreditLine <= maxCreditline){
            return creditIncreaseRequest.getIncreaseAmount();
        }
        else {
            return creditIncreaseRequest.getIncreaseAmount() - (totalCreditLine-maxCreditline);
        }
    }

}
