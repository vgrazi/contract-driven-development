import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/request-credit-increase'
        body """
        {
            "currentCreditLine" : 100000.0,
            "increaseAmount" : 1099000.0,
            "clientId" : 2
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
            "clientId" : 2,
            "increaseAmount" : 0
        }
        """
        headers {
            contentType applicationJson()
        }
    }
}
