package com.hotelbeds.supplierintegrations.hackertest.detector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SignInAttemptRecords {

    private final SignInHackerDetectorConfig config;

    private final Map<LocalDateTime, String> failedAttemptRecords = new HashMap<>();

    @Autowired
    public SignInAttemptRecords(SignInHackerDetectorConfig config) {
        this.config = config;
    }

    public void add(SignInAttempt signinAttempt) {
        failedAttemptRecords.put(signinAttempt.getTimestamp(), signinAttempt.getIp());
    }

    public void clean(String ip) {
        failedAttemptRecords.entrySet().removeIf(e -> e.getValue().equals(ip));
    }

    public void cleanExpired(LocalDateTime timestamp) {
        failedAttemptRecords.entrySet().removeIf(e -> e.getKey().isBefore(timestamp.minusMinutes(config.getAttemptsExpireMinutes())));
    }

    public long getCount(String ip) {
        return failedAttemptRecords.entrySet().stream().filter(e -> e.getValue().equals(ip)).count();
    }
}
