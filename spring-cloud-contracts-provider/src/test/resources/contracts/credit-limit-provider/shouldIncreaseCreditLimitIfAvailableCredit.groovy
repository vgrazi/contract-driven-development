import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url '/request-credit-increase'
        body(
                "currentCreditLine": $(consumer(anyDouble()), producer(100_000)),
                "increaseAmount": $(consumer(anyDouble()), producer(19_000)),
                "clientId": 1
                ,"taxId": "123-45-6789"
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
