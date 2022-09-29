package com.fintech.accessfintechdemo.dto;

import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PriceDTO {

    String name;
    double price;
}
