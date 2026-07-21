package io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.security;

import org.json.JSONObject;

public record RazorpaySignatureRequest(

        JSONObject attributes,

        String secret

) {
}
