package io.github.peeyushkumar.bookmyshow.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StripeProperties {
    private boolean enabled;

    private String apiKey;

    private String webhookSecret;

}
