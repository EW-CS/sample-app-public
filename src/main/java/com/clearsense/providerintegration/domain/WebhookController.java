package com.clearsense.providerintegration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebhookController {

    private final PushEventHandler handler;

    @PostMapping("/")
    @ResponseBody
    public void gitHubWebhook(
            @RequestHeader("X-GitHub-Event") String event,
            @RequestBody String payload
    ) {
        log.info("**************************");
        log.info("eventHeader: {}", event);
        log.info("Event-Payload: {}", payload);
        handler.handle(payload, event);
    }

    @GetMapping("/")
    @ResponseBody
    public String getGitHubWebhook() {
        return "Hello World";
    }
}
