package io.github.peeyushkumar.bookmyshow.exception.merchant;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class MerchantNotFoundException extends PaymentServiceException {

    public MerchantNotFoundException(String merchantId){
        super(HttpStatus.NOT_FOUND,
                ErrorCode.MERCHANT_NOT_FOUND,
                "Merchant not found: " + merchantId);
    }

}
