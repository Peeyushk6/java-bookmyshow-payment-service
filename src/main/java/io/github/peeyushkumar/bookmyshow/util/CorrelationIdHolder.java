package io.github.peeyushkumar.bookmyshow.util;

import org.slf4j.MDC;

public final class CorrelationIdHolder {

    private CorrelationIdHolder() {}

    public static final String CORRELATION_ID = "correlationId";

    public static void set(String value) {
        MDC.put(CORRELATION_ID, value);
    }

    public static String get() {
        return MDC.get(CORRELATION_ID);
    }

    public static void clear() {
        MDC.remove(CORRELATION_ID);
    }
}