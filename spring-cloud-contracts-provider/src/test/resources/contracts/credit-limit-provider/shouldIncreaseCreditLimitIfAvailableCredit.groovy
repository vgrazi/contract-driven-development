import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/request-credit-increase'
        body """
        {
            "currentCreditLine" : 1000000.0,
            "increaseAmount" : 120000.0
        }
        """
        headers {
            contentType applicationJson()
        }
    }
    response {
        status 200
        body """
        {
            "clientId" : 1,
            "increase" : 100000
        }
        """
        headers {
            contentType applicationJson()
        }
    }
}
