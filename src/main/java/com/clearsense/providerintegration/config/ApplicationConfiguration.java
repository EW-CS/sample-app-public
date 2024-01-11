package com.clearsense.providerintegration.config;

import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.logging.log4j.ThreadContext;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.zalando.logbook.*;
import org.zalando.logbook.json.JsonHttpLogFormatter;

import java.util.UUID;

import static com.clearsense.providerintegration.config.CorrelationRequestFilter.CORRELATION_ID;
import static org.zalando.logbook.Conditions.*;

@Slf4j
@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public FilterRegistrationBean<CorrelationRequestFilter> correlationIdRequestFilter() {
        val filter = new CorrelationRequestFilter();
        FilterRegistrationBean<CorrelationRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setAsyncSupported(true);

        return filterRegistrationBean;
    }

    /**
     * Logbook configuration: Leverage existing correlationId and filter /actuator requests
     */
    @Bean
    public Logbook logbook() {
        Logbook logbook = Logbook.builder()
                .condition(exclude(
                        requestTo("**/actuator/health/**"),
                        contentType("application/octet-stream")
                        ))
                .correlationId(new CustomLogbookCorrelationId())
                .sink(new DefaultSink(new JsonHttpLogFormatter(), new DefaultHttpLogWriter()))
                .build();
        return logbook;
    }


    class CustomLogbookCorrelationId implements CorrelationId {
        @Override
        public String generate(HttpRequest request) {
            return Option
                    .of(ThreadContext.getContext().get(CORRELATION_ID))
                    .getOrElse(UUID.randomUUID().toString());
        }
    }

}
