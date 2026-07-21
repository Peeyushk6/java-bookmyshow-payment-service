package io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.mapper;

import com.razorpay.Order;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayOrderResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class RazorpayOrderMapper {

    public JSONObject toCreateOrderRequest(
            GatewayCreateOrderRequest request) {
        JSONObject options = new JSONObject();

        options.put(
                "amount",
                request.getAmount()
                        .multiply(BigDecimal.valueOf(100))
                        .longValue()
        );

        options.put(
                "currency",
                request.getCurrency().name()
        );

        String receipt =
                request.getOrderReference();

        if(receipt.length() > 40){
            receipt = receipt.substring(0,40);
        }
        options.put("receipt", receipt);

        options.put(
                "notes",
                new JSONObject().put(
                        "merchantId",
                        request.getMerchantId()
                )
        );

        return options;

    }

    public GatewayOrderResponse toGatewayOrderResponse(Order order, Duration expiry) {

        return GatewayOrderResponse.builder()
                .providerOrderId(
                        order.get("id").toString()
                )
                .paymentStatus(GatewayPaymentStatus.CREATED)
                .gatewayMetadata(
                        order.toJson().toString(2)
                )
                .checkoutUrl("")
                .expiresAt(
                        LocalDateTime.now().plus(expiry)
                )
                .build();
    }

}
