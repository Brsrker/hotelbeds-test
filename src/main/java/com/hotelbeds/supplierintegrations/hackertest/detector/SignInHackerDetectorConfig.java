package com.hotelbeds.supplierintegrations.hackertest.detector;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:hackerDetector.properties")
public class SignInHackerDetectorConfig {
    @Value("${signIn.maxAttempts}")
    private int maxAttempts;
    @Value("${signIn.attemptsExpireMinutes}")
    private int attemptsExpireMinutes;
}
