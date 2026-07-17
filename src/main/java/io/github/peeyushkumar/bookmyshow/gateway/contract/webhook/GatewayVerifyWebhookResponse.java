package io.github.peeyushkumar.bookmyshow.gateway.contract.webhook;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatewayVerifyWebhookResponse
{
    private boolean verified;

    private String eventType;
}
