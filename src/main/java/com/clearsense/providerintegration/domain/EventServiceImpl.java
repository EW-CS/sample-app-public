package com.clearsense.providerintegration.domain;

import com.clearsense.providerintegration.config.EventServiceProperties;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.xml.transform.ErrorListener;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableConfigurationProperties(EventServiceProperties.class)
public class EventServiceImpl implements EventService {

    @Override
    public Either<ErrorListener, String> cloneRepo(String repoUrl) {
        return null;
    }
}
