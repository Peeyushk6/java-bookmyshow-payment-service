package io.github.peeyushkumar.bookmyshow.gateway.contract.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FetchPaymentRequest {
    private String providerPaymentId;
}
