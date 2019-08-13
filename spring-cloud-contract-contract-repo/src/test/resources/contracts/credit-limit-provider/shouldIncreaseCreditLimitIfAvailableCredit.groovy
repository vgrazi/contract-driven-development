import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/request-credit-increase'
        body """
        {
            "currentCreditLine" : 1000
            "increaseAmount" : 100_000
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
            "value" : "100000"
        }
        """
        headers {
            contentType textPlain()
        }
    }
}
