package io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.mapper;

import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayPaymentDetails;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class RazorpayVerificationMapper {

    public GatewayVerifyPaymentResponse toVerifyPaymentResponse(
            GatewayPaymentDetails details,
            boolean signatureVerified
    ) {

        return GatewayVerifyPaymentResponse.builder()
                .signatureVerified(signatureVerified)
                .paymentStatus(details.getStatus())
                .providerPaymentId(details.getProviderPaymentId())
                .providerOrderId(details.getProviderOrderId())
                .paymentMethod(details.getPaymentMethod())
                .currency(details.getCurrency())
                .amount(details.getAmount())
                .gatewayMetadata(details.getGatewayMetadata())
                .message(signatureVerified
                        ? "Payment verified successfully."
                        : "Signature verification failed.")
                .build();

    }
}
