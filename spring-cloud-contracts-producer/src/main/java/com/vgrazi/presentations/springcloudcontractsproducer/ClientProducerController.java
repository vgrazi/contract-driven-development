package com.vgrazi.presentations.springcloudcontractsproducer;

import com.vgrazi.presentations.springcloudcontractsproducer.gateway.CreditIncreaseRequest;
import com.vgrazi.presentations.springcloudcontractsproducer.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientProducerController {

    @Value("${max-credit-line}")
    private double maxCreditline;
    @PostMapping("/request-credit-increase")
    public Double handleCreditIncreaseRequest(@RequestBody CreditIncreaseRequest creditIncreaseRequest){
        double currentCreditLine = creditIncreaseRequest.getCurrentCreditLine();
        double totalCreditLine = currentCreditLine + creditIncreaseRequest.getIncreaseAmount();
//        if(true) {
//            return 0.0;
//        }
        double rval = 0;
        if (totalCreditLine > maxCreditline) {
            double revised = creditIncreaseRequest.getIncreaseAmount() - (totalCreditLine - maxCreditline);
            if(revised > 0) {
                rval = revised;
            }
            else {
                revised = maxCreditline - currentCreditLine;
                if(revised > 0) {
                    // can't give em what they want - give em what they can have
                    rval = revised;
                }
                if (rval == 0) {
                    throw new IllegalArgumentException("Credit line has reached its max");
                }
            }
        } else {
            rval = creditIncreaseRequest.getIncreaseAmount();
        }
        double tryAgain = Utils.round(rval, 10_000);
        if(tryAgain + currentCreditLine <= maxCreditline) {
            rval = tryAgain;
        }
        return rval;
    }

}
