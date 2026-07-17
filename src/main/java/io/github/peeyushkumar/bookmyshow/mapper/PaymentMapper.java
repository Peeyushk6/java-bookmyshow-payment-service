package io.github.peeyushkumar.bookmyshow.mapper;

import io.github.peeyushkumar.bookmyshow.dto.response.PaymentResponse;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.gateway.dto.request.GatewayPaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public GatewayPaymentRequest toGatewayRequest(Payment payment) {

        return GatewayPaymentRequest.builder()
                .paymentId(payment.getId().toString())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .description(payment.getDescription())
                .referenceId(payment.getReferenceId())
                .build();
    }

    public PaymentResponse toResponse(Payment payment){

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .provider(payment.getProvider())
                .providerOrderId(payment.getProviderOrderId())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .build();

    }

}