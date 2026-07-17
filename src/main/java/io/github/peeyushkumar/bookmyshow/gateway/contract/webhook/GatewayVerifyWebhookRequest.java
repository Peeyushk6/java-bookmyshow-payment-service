package io.github.peeyushkumar.bookmyshow.gateway.contract.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GatewayVerifyWebhookRequest {

    private String signature;

    private String payload;
}
