import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/request-credit-increase'
        body (
            "currentCreditLine" : 100000.0,
            "increaseAmount" : 1099000.0,
            "clientId" : 2
        )
        headers {
            contentType applicationJson()
        }
    }
    response {
        status 200
        body (
            "clientId" : 2,
            "increaseAmount" : 0,
            "denialReason" : "Credit line has reached its max. Available: 900000.0",
                "date": $(producer(anyDate()), consumer("2019-08-14"))

        )
        headers {
            contentType applicationJson()
        }
    }
}
