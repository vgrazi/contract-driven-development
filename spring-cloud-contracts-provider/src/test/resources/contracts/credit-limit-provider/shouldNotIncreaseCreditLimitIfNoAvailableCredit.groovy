import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/request-credit-increase'
        body (
                "currentCreditLine": $(consumer(anyDouble()), producer(100_000)),
                "increaseAmount": $(consumer(anyDouble()), producer(1_099_000)),
                "clientId": 1
        )
        headers {
            contentType applicationJson()
        }
    }
    response {
        status 200
        body (
            "clientId": fromRequest().body('$.clientId'),
            "increaseAmount" : 0,
            "denialReason" : "Credit line has reached its max. Available: 900000.0",
                "date": $(producer(anyDate()), consumer("2019-08-14"))

        )
        headers {
            contentType applicationJson()
        }
    }
}
