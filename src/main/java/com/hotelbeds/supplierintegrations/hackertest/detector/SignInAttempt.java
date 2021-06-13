package com.hotelbeds.supplierintegrations.hackertest.detector;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SignInAttempt {
    private String ip;
    private LocalDateTime timestamp;
    private String status;
}
