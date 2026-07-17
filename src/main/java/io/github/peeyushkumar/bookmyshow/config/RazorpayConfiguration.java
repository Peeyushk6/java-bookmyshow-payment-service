package io.github.peeyushkumar.bookmyshow.config;

import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RazorpayConfiguration {

    private final PaymentProviderProperties properties;

    @Bean
    public RazorpayClient razorpayClient() throws Exception{

        RazorpayProperties razorpay = properties.getRazorpay();

        if(!razorpay.isEnabled()){
            return null;
        }

        return new RazorpayClient(
                razorpay.getKeyId(),
                razorpay.getKeySecret()
        );
    }
}
