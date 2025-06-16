package com.example.demo.common.service.port;

import org.springframework.stereotype.Component;

@Component
public interface ClockHolder {
    long millis();
}
