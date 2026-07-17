package io.github.peeyushkumar.bookmyshow.gateway;

import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayOrderResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayPaymentDetails;
import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.gateway.contract.refund.GatewayRefundRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.webhook.GatewayVerifyWebhookRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.refund.GatewayRefundResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.webhook.GatewayVerifyWebhookResponse;

public interface PaymentGateway {

    PaymentProvider getProvider();

    GatewayOrderResponse createOrder(
            GatewayCreateOrderRequest request
    );

    GatewayVerifyPaymentResponse verifyPayment(
            GatewayVerifyPaymentRequest request
    );

    GatewayPaymentDetails fetchPayment(
            String providerPaymentId
    );

    GatewayRefundResponse refund(
            GatewayRefundRequest request
    );

    GatewayVerifyWebhookResponse verifyWebhook(
            GatewayVerifyWebhookRequest request
    );
}
