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
                "increaseAmount": $(consumer(anyDouble()), producer(1_099_000)),
                "clientId": 2,
                "date": $(producer("2019-08-14"), consumer(anyDate()))
        )
        headers {
            contentType applicationJson()
        }
    }
    response {
        status 200
        body(
                "clientId": fromRequest().body('$.clientId'),
                "increaseAmount": 0,
                "denialReason": "Credit line has reached its max. Available: 900000.0",
                "date": $(producer(anyDate()), consumer("2019-08-14"))
        )
        headers {
            contentType applicationJson()
        }
    }
}
