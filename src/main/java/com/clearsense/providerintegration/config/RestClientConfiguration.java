package com.clearsense.providerintegration.config;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@AllArgsConstructor
public class RestClientConfiguration {

    private final Logbook logbook;

    @Bean
    public RestTemplate restTemplate(LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor){
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = Option
                .of(restTemplate.getInterceptors())
                .getOrElse(new ArrayList<>());

            // ensures correlationId propogration across services
            interceptors.add(new RestTemplateHeaderModifierInterceptor());
            restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }


    class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body,
                                            ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            String correlationID = Option
                    .of(ThreadContext.getContext().get(CorrelationRequestFilter.CORRELATION_ID))
                    .getOrElse(UUID.randomUUID().toString());

            httpRequest.getHeaders().set(CorrelationRequestFilter.CORRELATION_ID, correlationID);

            return clientHttpRequestExecution.execute(httpRequest, body);
        }
    }
}
