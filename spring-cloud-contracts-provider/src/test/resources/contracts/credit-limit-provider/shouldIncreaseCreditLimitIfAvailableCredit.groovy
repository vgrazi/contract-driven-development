import org.springframework.cloud.contract.spec.Contract

import java.time.LocalDateTime
import java.time.ZoneOffset

def seconds = LocalDateTime.of(2019, 9, 2, 8, 0).toEpochSecond(ZoneOffset.MIN)

Contract.make {
    request {
        method 'POST'
        url '/request-credit-increase'
        body(
                "currentCreditLine": $(consumer(anyDouble()), producer(100_000)),
                "increaseAmount": $(consumer(anyDouble()), producer(19_000)),
                "clientId": 1,
                "date": $(producer("2019-08-14"), consumer(anyDate()))
// Bookmark 7
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
                "denialReason": null
        )
        headers {
            contentType applicationJson()
        }
    }
}
