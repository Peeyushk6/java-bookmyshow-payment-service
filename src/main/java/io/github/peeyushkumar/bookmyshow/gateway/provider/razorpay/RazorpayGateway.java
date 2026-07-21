package io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay;

import com.razorpay.Order;
import com.razorpay.Payment;
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
import io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.mapper.RazorpayOrderMapper;
import io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.mapper.RazorpayPaymentDetailsMapper;
import io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.mapper.RazorpayVerificationMapper;
import io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.security.RazorpaySignatureVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RazorpayGateway implements PaymentGateway {
    private final PaymentProviderProperties properties;

    private final RazorpayClient razorpayClient;

    private final RazorpayOrderMapper razorpayOrderMapper;


    private final RazorpaySignatureVerifier signatureVerifier;

    private final RazorpayPaymentDetailsMapper razorpayPaymentDetailsMapper;

    private final RazorpayVerificationMapper razorpayVerificationMapper;

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.RAZORPAY;
    }

    @Override
    public GatewayOrderResponse createOrder(GatewayCreateOrderRequest request) {
        try {

            JSONObject options = razorpayOrderMapper.toCreateOrderRequest(request);

            Duration expiry =
                    properties.getRazorpay().getOrderExpiry();

            Order order =
                    razorpayClient.orders.create(options);
            String orderId = order.get("id").toString();

            log.info(
                    "Created Razorpay Order {}",
                    orderId
            );


            return razorpayOrderMapper.toGatewayOrderResponse(order,expiry);
        }
        catch (RazorpayException exception){
            log.error(
                    "Failed to create Razorpay Order",
                    exception
            );

            throw new PaymentGatewayException(
                    "Unable to create Razorpay order",
                    exception
            );
        }
    }

    @Override
    public GatewayVerifyPaymentResponse verifyPayment(GatewayVerifyPaymentRequest request) {
        try{
            boolean signatureVerified =
                    signatureVerifier.verifyPaymentSignature(request);


            GatewayPaymentDetails details =
                    razorpayPaymentDetailsMapper
                            .toGatewayPaymentDetails(fetchRazorpayPayment
                                    (request.getProviderPaymentId()));

            return razorpayVerificationMapper
                    .toVerifyPaymentResponse(details,signatureVerified);
        }
        catch (RazorpayException exception){
            log.error(
                    "Payment verification failed. paymentId={}",
                    request.getProviderPaymentId(),
                    exception
            );

            throw new PaymentGatewayException(
                    "Unable to verify payment.",
                    exception
            );
        }


    }

    @Override
    public GatewayPaymentDetails fetchPayment(String providerPaymentId) {
        try{

            return razorpayPaymentDetailsMapper
                    .toGatewayPaymentDetails(fetchRazorpayPayment(providerPaymentId));

        }
        catch (RazorpayException exception){
            log.error(
                    "Unable to fetch Razorpay payment. paymentId={}",
                    providerPaymentId,
                    exception
            );

            throw new PaymentGatewayException(
                    "Unable to fetch payment details.",
                    exception
            );
        }
    }

    @Override
    public GatewayRefundResponse refund(GatewayRefundRequest request) {
        return null;
    }

    @Override
    public GatewayVerifyWebhookResponse verifyWebhook(GatewayVerifyWebhookRequest request) {
        return null;
    }

    private Payment fetchRazorpayPayment(
            String providerPaymentId
    ) throws RazorpayException {

        return razorpayClient
                .payments
                .fetch(providerPaymentId);
    }
}
