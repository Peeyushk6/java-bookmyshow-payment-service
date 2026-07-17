package io.github.peeyushkumar.bookmyshow.exception.merchant;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class MerchantInactiveException extends PaymentServiceException {

    public MerchantInactiveException(String merchantId){
        super(HttpStatus.FORBIDDEN,
                ErrorCode.MERCHANT_INACTIVE,
                "Merchant not found: " + merchantId);
    }

}
