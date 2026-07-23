package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.exception.merchant.MerchantInactiveException;
import io.github.peeyushkumar.bookmyshow.exception.merchant.MerchantNotFoundException;
import io.github.peeyushkumar.bookmyshow.repository.MerchantRepository;
import io.github.peeyushkumar.bookmyshow.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;

    @Override
    public Merchant getActiveMerchant(String merchantId) {

        Merchant merchant = merchantRepository
                .findByMerchantId(merchantId)
                .orElseThrow(() ->
                        new MerchantNotFoundException(merchantId));

        if (!merchant.isActive()) {
            throw new MerchantInactiveException(merchantId);
        }

        return merchant;
    }

}
