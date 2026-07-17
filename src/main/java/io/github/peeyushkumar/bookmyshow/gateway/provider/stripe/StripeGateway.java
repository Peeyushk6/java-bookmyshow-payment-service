package io.github.peeyushkumar.bookmyshow.gateway.provider.stripe;

import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.gateway.PaymentGateway;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.FetchPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayOrderResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayPaymentDetails;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.refund.GatewayRefundRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.refund.GatewayRefundResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.webhook.GatewayVerifyWebhookRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.webhook.GatewayVerifyWebhookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StripeGateway implements PaymentGateway {

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.STRIPE;
    }

    @Override
    public GatewayOrderResponse createOrder(GatewayCreateOrderRequest request) {
        return null;
    }

    @Override
    public GatewayVerifyPaymentResponse verifyPayment(GatewayVerifyPaymentRequest request) {
        return null;
    }

    @Override
    public GatewayPaymentDetails fetchPayment(String providerPaymentId) {
        return null;
    }

    @Override
    public GatewayRefundResponse refund(GatewayRefundRequest request) {
        return null;
    }

    @Override
    public GatewayVerifyWebhookResponse verifyWebhook(GatewayVerifyWebhookRequest request) {
        return null;
    }
}
