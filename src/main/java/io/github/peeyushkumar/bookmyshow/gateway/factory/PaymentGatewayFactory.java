package io.github.peeyushkumar.bookmyshow.gateway.factory;

import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.gateway.PaymentGateway;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentGatewayFactory {

    private final List<PaymentGateway> gateways;

    private final EnumMap<PaymentProvider, PaymentGateway> gatewayMap =
            new EnumMap<>(PaymentProvider.class);

/*
    when Spring sees the constructor public PaymentGatewayFactory(
            List<PaymentGateway> gateways
    ) which is created by the RequiredArgsConstructor Spring automatically creates as they were implemented class of PaymentGateway
    List<PaymentGateway> gateways =
            List.of(
                    razorpayGateway,
                    stripeGateway
            );

This is a feature of Spring.
Whenever Spring sees
List<Interface>
it injects every bean implementing that interface.
*/



/*    Spring creates the object.
            ↓
    Injects dependencies.
            ↓
    Calls
    Run this after Dependency Injection has finished.*/

    @PostConstruct
    public void initialize() {

        for (PaymentGateway gateway : gateways) {
            gatewayMap.put(gateway.getProvider(), gateway);
        }
    }

    public PaymentGateway getGateway(
            PaymentProvider provider
    ) {

        PaymentGateway gateway = gatewayMap.get(provider);

        if (gateway == null) {
            throw new IllegalArgumentException(
                    "Unsupported payment provider : " + provider
            );
        }

        return gateway;
    }
}
