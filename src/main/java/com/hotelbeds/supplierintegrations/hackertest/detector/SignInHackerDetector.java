package com.hotelbeds.supplierintegrations.hackertest.detector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class SignInHackerDetector implements HackerDetector {

    private final static String SIGN_IN_SUCCESS = "SIGNIN_SUCCESS";
    private final static String SIGN_IN_FAILURE = "SIGNIN_FAILURE";
    private final SignInAttemptRecords signInAttemptRecords;
    private final SignInHackerDetectorConfig config;

    @Autowired
    public SignInHackerDetector(SignInAttemptRecords signInAttemptRecords, SignInHackerDetectorConfig config) {
        this.signInAttemptRecords = signInAttemptRecords;
        this.config = config;
    }

    @Override
    public String parseLine(String line) {
        SignInAttempt attempt = toObject(line);
        signInAttemptRecords.cleanExpired(attempt.getTimestamp());
        switch (attempt.getStatus()) {
            case SIGN_IN_FAILURE:
                signInAttemptRecords.add(attempt);
                long failedAttemptCount = signInAttemptRecords.getCount(attempt.getIp());
                if (failedAttemptCount >= config.getMaxAttempts()) {
                    return attempt.getIp();
                }
                break;
            case SIGN_IN_SUCCESS:
                signInAttemptRecords.clean(attempt.getIp());
                break;
        }
        return null;
    }

    private SignInAttempt toObject(String line) {
        String[] strings = line.split(",");
        long epoch = Long.parseLong(strings[1]);
        LocalDateTime ldt = toLocalDateTime(epoch);
        SignInAttempt signinAttempt = new SignInAttempt();
        signinAttempt.setIp(strings[0]);
        signinAttempt.setTimestamp(ldt);
        signinAttempt.setStatus(strings[2]);
        return signinAttempt;
    }

    private LocalDateTime toLocalDateTime(long time) {
        Instant instant = Instant.ofEpochMilli(time);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }

}
