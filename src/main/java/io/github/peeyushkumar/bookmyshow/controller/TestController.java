package io.github.peeyushkumar.bookmyshow.controller;

import io.github.peeyushkumar.bookmyshow.config.PaymentProviderProperties;
import io.github.peeyushkumar.bookmyshow.config.RazorpayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final PaymentProviderProperties properties;

//    @GetMapping
//    public PaymentProviderProperties test() {
//        return properties;
//    }

    @GetMapping()
    public RazorpayProperties config() {
        return properties.getRazorpay();
    }

}
