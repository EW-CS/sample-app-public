package com.clearsense.providerintegration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class ServerStartedInitializingBean implements ApplicationRunner, ApplicationListener<WebServerInitializedEvent> {

    private int localPort;

    public int getLocalPort() {
        return localPort;
    }

    @Override
    public void run(ApplicationArguments aa) throws Exception {
        log.info("server started with args: {}", Arrays.toString(aa.getSourceArgs()));
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        localPort = event.getWebServer().getPort();
        log.info("after runtime init, local server port: {}", localPort);
    }
}
