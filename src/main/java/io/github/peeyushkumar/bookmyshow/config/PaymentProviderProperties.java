package io.github.peeyushkumar.bookmyshow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment.providers")
@Getter
@Setter
public class PaymentProviderProperties {

    private RazorpayProperties razorpay;

    private StripeProperties stripe;
}
