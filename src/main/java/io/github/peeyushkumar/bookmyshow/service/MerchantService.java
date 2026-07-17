package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.entity.Merchant;

public interface MerchantService {

    Merchant getActiveMerchant(String merchantId);

}

