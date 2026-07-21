package io.github.peeyushkumar.bookmyshow.mapper;

import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayMapper {

    public GatewayCreateOrderRequest toGatewayRequest(Payment payment) {

        Merchant merchant = payment.getMerchant();

        return GatewayCreateOrderRequest.builder()
                .merchantId(merchant.getMerchantId())
                .orderReference(payment.getReferenceId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .description(payment.getDescription())
                .callbackUrl(merchant.getCallbackUrl())
                .build();
    }

    public GatewayVerifyPaymentRequest toGatewayVerifyPaymentRequest(
            PaymentVerificationRequest request
    ) {

        return GatewayVerifyPaymentRequest.builder()
                .providerOrderId(request.providerOrderId())
                .providerPaymentId(request.providerPaymentId())
                .gatewaySignature(request.providerSignature())
                .build();

    }
}