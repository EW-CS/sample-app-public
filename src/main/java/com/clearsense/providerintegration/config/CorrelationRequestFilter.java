package com.clearsense.providerintegration.config;

import com.google.common.collect.Iterators;
import io.vavr.control.Option;
import lombok.val;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Correlation Request filter to checking CorrelationID in incoming request;
 * if not available generates correlationID
 */
public class CorrelationRequestFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID = "CorrelationId";
    private static final Predicate<String> IS_CORRELATION_HEADER = name -> CORRELATION_ID.equals(name);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        val correlationId = Option
                .of(httpServletRequest.getHeader(CORRELATION_ID))
                .getOrElse(() -> UUID.randomUUID().toString());

        val correlatedRequest = new CorrelatedHttpServletRequest(httpServletRequest, correlationId);
        httpServletResponse.setHeader(CORRELATION_ID, correlationId);
        MDC.put(CORRELATION_ID, correlationId);

        filterChain.doFilter(correlatedRequest, httpServletResponse);
    }

    private static class CorrelatedHttpServletRequest extends HttpServletRequestWrapper {
        private final String correlationId;
        private final Predicate<String> notCorrelationHeader;

        public CorrelatedHttpServletRequest(HttpServletRequest request, String correlationId) {
            super(request);
            this.correlationId = correlationId;
            this.notCorrelationHeader = Predicate.not(IS_CORRELATION_HEADER);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Enumeration<String> headerNames = super.getHeaderNames();
            Stream<String> withoutCorrelationHeaders = StreamSupport
                    .stream(Spliterators.spliteratorUnknownSize(headerNames.asIterator(), Spliterator.ORDERED), false)
                    .filter(notCorrelationHeader);
            Iterator<String> correlated = Stream
                    .concat(withoutCorrelationHeaders,
                            Stream.of(CORRELATION_ID))
                    .iterator();

            return Iterators.asEnumeration(correlated);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            Enumeration<String> headers = super.getHeaders(name);
            if(IS_CORRELATION_HEADER.test(name))
                headers = Collections.enumeration(Collections.singletonList(correlationId));
            return headers;
        }

        @Override
        public String getHeader(String name) {
            String header = super.getHeader(name);
            if(IS_CORRELATION_HEADER.test(name))
                header = correlationId;
            return header;
        }
    }
}
