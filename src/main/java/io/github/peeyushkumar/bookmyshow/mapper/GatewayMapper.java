package io.github.peeyushkumar.bookmyshow.mapper;

import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import org.springframework.stereotype.Component;

@Component
public class GatewayMapper {

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
}