package io.github.peeyushkumar.bookmyshow.exception.gateway;

import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnsupportedPaymentProviderException
        extends PaymentServiceException {

    public UnsupportedPaymentProviderException(
            PaymentProvider provider
    ) {
        super(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ErrorCode.UNSUPPORTED_PAYMENT_PROVIDER,
                "Unsupported payment provider : " + provider
        );
    }
}