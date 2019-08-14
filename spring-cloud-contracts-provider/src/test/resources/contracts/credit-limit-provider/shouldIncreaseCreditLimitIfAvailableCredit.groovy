import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/request-credit-increase'
        body(
                "currentCreditLine": 100000.0,
                "increaseAmount": 19000.0,
                "clientId": 1
        )
        headers {
            contentType applicationJson()
        }
    }
    response {
        status 200
        body(
                "clientId": fromRequest().body('$.clientId'),
                "increaseAmount": 20000,
                "denialReason": null,
                "date": $(producer(anyDate()), consumer("2019-08-14"))
        )
        headers {
            contentType applicationJson()
        }
    }
}
