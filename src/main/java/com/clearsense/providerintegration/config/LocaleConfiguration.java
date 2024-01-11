package com.clearsense.providerintegration.config;

import io.vavr.control.Option;
import lombok.val;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Configuration
public class LocaleConfiguration {

    private static final String ACCEPT_LANGUAGE = "Accept-Language";

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    @Bean
    public ResourceBundleMessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("contents/labels");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public FilterRegistrationBean localeRequestFilter(){
        val filter = new LocaleRequestFilter();
        FilterRegistrationBean<LocaleRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setAsyncSupported(true);

        return filterRegistrationBean;
    }

    public static class LocaleRequestFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
            Locale locale = null;
            try {
                locale = StringUtils.parseLocaleString(
                        Option.of(httpServletRequest.getHeader(ACCEPT_LANGUAGE))
                                .getOrElse(Locale.getDefault().getLanguage())
                );

                ThreadContext.put(ACCEPT_LANGUAGE, locale.toString());
            } catch (Exception e) {
                locale = Locale.getDefault();
                ThreadContext.put(ACCEPT_LANGUAGE, locale.getLanguage());
            }

            httpServletResponse.setHeader(ACCEPT_LANGUAGE, locale.toString());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}