package com.clearsense.providerintegration.adapters.locale;

import io.vavr.control.Option;
import lombok.Setter;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Locale;

public class LocaleUtils {


    private @Setter MessageSource messageSource;
    private static final String ACCEPT_LANGUAGE = "Accept-Language";
    private static LocaleUtils localeUtils;

    private LocaleUtils() { }

    @Component
    public static class MessageSourceInjector {

        @Autowired
        private MessageSource messageSource;

        @PostConstruct
        public void postConstruct() {
            LocaleUtils instance = LocaleUtils.getInstance();
            instance.setMessageSource(messageSource);
        }
    }

    public static LocaleUtils getInstance(){
        if (localeUtils == null){
            localeUtils = new LocaleUtils();
        }
        return localeUtils;
    }

    public static String getMessage(String key, Object[] objects, String s1) {
        return getInstance()
                .messageSource
                .getMessage(
                        key,objects,
                        s1, getLocale());
    }

    public static String getMessage(String key, Object[] objects) throws NoSuchMessageException {
        return getInstance()
                .messageSource
                .getMessage(key, objects, getLocale());
    }


    public static Locale getLocale(){
        String language = null;
        try {
            language = Option
                    .of(ThreadContext.getContext().get(ACCEPT_LANGUAGE))
                    .getOrElse(() -> Locale.getDefault().toLanguageTag());
        } catch (Exception e) {
            language = Locale.getDefault().toLanguageTag();
        }

        return StringUtils.parseLocaleString(language);
    }
}
