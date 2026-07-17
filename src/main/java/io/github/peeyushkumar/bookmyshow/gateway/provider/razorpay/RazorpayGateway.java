package io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import io.github.peeyushkumar.bookmyshow.config.PaymentProviderProperties;
import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.exception.gateway.PaymentGatewayException;
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
import io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.mapper.RazorpayMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class RazorpayGateway implements PaymentGateway {
    private final PaymentProviderProperties properties;

    private final RazorpayClient razorpayClient;

    private final RazorpayMapper razorpayMapper;

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.RAZORPAY;
    }

    @Override
    public GatewayOrderResponse createOrder(GatewayCreateOrderRequest request) {
        try {

            JSONObject options = razorpayMapper.toCreateOrderRequest(request);

            Duration expiry =
                    properties.getRazorpay().getOrderExpiry();

            Order order =
                    razorpayClient.orders.create(options);
            System.out.println( "Created Razorpay Order : "+
                    order.get("id"));


            return razorpayMapper.toGatewayOrderResponse(order,expiry);
        }
        catch (RazorpayException exception){
            System.out.println(
                    "Failed to create Razorpay Order" +
                    exception
            );

            throw new PaymentGatewayException(
                    "Unable to create Razorpay order"
            );
        }
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
