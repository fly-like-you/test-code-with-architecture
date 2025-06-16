package com.example.demo.common.infrastructure;

import com.example.demo.common.service.port.ClockHolder;

public class SystemClockHolder implements ClockHolder {
    @Override
    public long millis() {
        return System.currentTimeMillis();
    }
}
