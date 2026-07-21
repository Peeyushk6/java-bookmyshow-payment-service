package io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.security;

import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import io.github.peeyushkumar.bookmyshow.config.PaymentProviderProperties;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RazorpaySignatureVerifier {
    private final PaymentProviderProperties properties;

    public boolean verifyPaymentSignature(
            GatewayVerifyPaymentRequest request
    ) throws RazorpayException {

        JSONObject attributes = new JSONObject();

        attributes.put(
                "razorpay_order_id",
                request.getProviderOrderId()
        );

        attributes.put(
                "razorpay_payment_id",
                request.getProviderPaymentId()
        );

        attributes.put(
                "razorpay_signature",
                request.getGatewaySignature()
        );

        return Utils.verifyPaymentSignature(
                attributes,
                properties.getRazorpay().getKeySecret()
        );
    }
}
