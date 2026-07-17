package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.dto.request.CreatePaymentRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentResponse;
import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.factory.PaymentFactory;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayOrderResponse;
import io.github.peeyushkumar.bookmyshow.gateway.router.PaymentGatewayRouter;
import io.github.peeyushkumar.bookmyshow.mapper.GatewayMapper;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentMapper;
import io.github.peeyushkumar.bookmyshow.service.MerchantService;
import io.github.peeyushkumar.bookmyshow.service.PaymentPersistenceService;
import io.github.peeyushkumar.bookmyshow.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final MerchantService merchantService;

    private final PaymentPersistenceService paymentPersistenceService;

    private final PaymentFactory paymentFactory;

    private final GatewayMapper gatewayMapper;

    private final PaymentGatewayRouter paymentGatewayRouter;

    private final PaymentMapper paymentMapper;

    public PaymentResponse createPayment(CreatePaymentRequest request){
        // 1. Validate Merchant
        Merchant merchant = merchantService.getActiveMerchant(request.getMerchantId());

        // 2. True Idempotency
        Payment existingPayment =
                paymentPersistenceService.findByMerchantAndIdempotencyKey(merchant,request.getIdempotencyKey()).orElse(null);

        if (existingPayment != null) {
            return paymentMapper.toResponse(
                    existingPayment
            );
        }

        // 3. Create Payment Entity
        Payment payment = paymentFactory.create(
                merchant,
                request
        );

        // 4. Persist INITIATED Payment

        payment = paymentPersistenceService.save(payment);


        // 5. Build to Gateway Request

        GatewayCreateOrderRequest gatewayRequest =
                gatewayMapper.toGatewayRequest(payment);

        // 6. Create Gateway Order
        GatewayOrderResponse gatewayResponse =
                paymentGatewayRouter.createOrder(
                        payment.getProvider(),
                        gatewayRequest
                );

        // 7. Update Payment
        payment.markProcessing(gatewayResponse);

        // 8. Persist Updated Payment
        payment = paymentPersistenceService.save(payment);

        // 9. Return Response
        return paymentMapper.toResponse(payment);
    }
}
