package io.github.peeyushkumar.bookmyshow.config;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class RazorpayProperties {
    private boolean enabled;

    private String keyId;

    private String keySecret;

    private String webhookSecret;

    private Duration orderExpiry;

}
