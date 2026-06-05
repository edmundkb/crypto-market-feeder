package com.crypto.alerts.market.feeder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TickerPayload(
        @JsonProperty("product_id")
        String productId,
        String price
) {}
