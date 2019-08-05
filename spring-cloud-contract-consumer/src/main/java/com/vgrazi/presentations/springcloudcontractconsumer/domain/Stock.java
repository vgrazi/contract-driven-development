package com.vgrazi.presentations.springcloudcontractconsumer.domain;

import lombok.*;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class Stock {
    @Getter @Setter
    private String symbol;
    @Getter @Setter
    private String exchange;
}
