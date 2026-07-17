package io.github.peeyushkumar.bookmyshow.filter;

import io.github.peeyushkumar.bookmyshow.util.CorrelationIdHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
    private static final String HEADER = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException{
        String correlationId = request.getHeader(HEADER);

        if(correlationId == null || correlationId.isBlank()){
            correlationId = UUID.randomUUID().toString();
        }

        CorrelationIdHolder.set(correlationId);

        response.setHeader(
                HEADER,
                correlationId
        );

      /*  Think of filters as a chain.
        CorrelationIdFilter
↓
        AuthenticationFilter
↓
        RateLimitFilter
↓
        Controller
        When you call
        filterChain.doFilter(request, response);
        you're saying:
        "I'm done with my work. Pass the request to the next filter."
        Eventually it reaches the controller.
        When the controller finishes, execution unwinds back through the filters. */

        try{
            filterChain.doFilter(request,response);
        }
        finally {
            CorrelationIdHolder.clear();        }

    }
}
