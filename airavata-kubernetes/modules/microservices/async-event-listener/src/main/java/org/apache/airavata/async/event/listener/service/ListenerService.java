package org.apache.airavata.async.event.listener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class ListenerService {

    private final RestTemplate restTemplate;

    @Value("${api.server.url}")
    private String apiServerUrl;

    public ListenerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void onEventReceived(long workflowId, String event, String message) {
        Map<String, String> boostrapData = new HashMap<>();
        boostrapData.put("event", event);
        boostrapData.put("message", message);
        this.restTemplate.postForObject("http://" + apiServerUrl + "/workflow/" + workflowId + "/launch", boostrapData, Long.class);
    }
}
