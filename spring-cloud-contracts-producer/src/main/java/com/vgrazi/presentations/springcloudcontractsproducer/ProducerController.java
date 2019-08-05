package com.vgrazi.presentations.springcloudcontractsproducer;

import com.vgrazi.presentations.springcloudcontractsproducer.gateway.PricingRequest;
import com.vgrazi.presentations.springcloudcontractsproducer.gateway.PricingResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ProducerController {

    @PostMapping(value = "/pricing", consumes = APPLICATION_JSON_VALUE)
    public PricingResponse pricing(@RequestBody PricingRequest request) {
        return null;
    }

}
