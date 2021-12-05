package io.monitoring.controller;

import io.monitoring.model.payload.MessageResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> test() throws InterruptedException {
        int delay = 15;
        Thread.sleep(delay * 1000);
        return ResponseEntity.ok(new MessageResponse("Responded after " + delay + " seconds delay"));
    }
}
