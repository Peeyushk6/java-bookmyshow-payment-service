package io.github.peeyushkumar.bookmyshow.gateway.router;

import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.gateway.PaymentGateway;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayOrderResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayPaymentDetails;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.refund.GatewayRefundRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.refund.GatewayRefundResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.webhook.GatewayVerifyWebhookRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.webhook.GatewayVerifyWebhookResponse;
import io.github.peeyushkumar.bookmyshow.gateway.factory.PaymentGatewayFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter
{

    private final PaymentGatewayFactory gatewayFactory;

    private PaymentGateway gateway(PaymentProvider provider){
        return gatewayFactory.getGateway(provider);
    }

    public GatewayOrderResponse createOrder(
            PaymentProvider provider,
            GatewayCreateOrderRequest request
    ){
        return gateway(provider).createOrder(request);
    }

    public GatewayVerifyPaymentResponse verifyPayment(
            PaymentProvider provider,
            GatewayVerifyPaymentRequest request
    ){
        return gateway(provider).verifyPayment(request);
    }

    public GatewayPaymentDetails fetchPayment(
            PaymentProvider provider,
            String providerPaymentId
    ){
        return gateway(provider)
                .fetchPayment(providerPaymentId);
    }

    public GatewayRefundResponse refund(
            PaymentProvider provider,
            GatewayRefundRequest request
    ){
        return gateway(provider)
                .refund(request);
    }

    public GatewayVerifyWebhookResponse verifyWebhook(
            PaymentProvider provider,
            GatewayVerifyWebhookRequest request
    ){
        return gateway(provider)
                .verifyWebhook(request);
    }
}
