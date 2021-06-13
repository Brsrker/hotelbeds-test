package com.hotelbeds.supplierintegrations.hackertest.detector;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignInHackerDetectorTest {

    @Autowired
    private SignInHackerDetectorConfig config;

    private HackerDetector signInHackerDetectorTest;

    @BeforeEach
    public void setUp() {
        signInHackerDetectorTest = new SignInHackerDetector(new SignInAttemptRecords(config), config);
    }

    @Test
    @Order(1)
    public void testParseLine_whenSignInSuccess_thenNull() {
        String line = "80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith";
        String ip = signInHackerDetectorTest.parseLine(line);
        assertNull(ip);
    }

    @Test
    @Order(2)
    public void testParseLine_whenInvalidEpochTimestamp_thenNumberFormatException() {
        String line = "80.238.9.179,INVALID,SIGNIN_SUCCESS,Will.Smith";
        assertThrows(NumberFormatException.class, () -> signInHackerDetectorTest.parseLine(line));
    }

    @Test
    @Order(3)
    public void testParseLine_whenSignInFailure5TimesIn5MinInSameIp_thenIP() {
        List<String> lines = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        lines.add("80.238.9.179," + toMilli(now.minusMinutes(5).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(4).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(3).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(2).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(1).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");

        String ip = null;
        for (String line : lines) {
            ip = signInHackerDetectorTest.parseLine(line);
            if (ip != null) {
                break;
            }
        }

        assertEquals("80.238.9.179", ip);
    }

    @Test
    @Order(4)
    public void testParseLine_whenSignInFailure4AndSuccess1TimesIn5MinInSameIp_thenNull() {
        List<String> lines = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        lines.add("80.238.9.179," + toMilli(now.minusMinutes(5).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(4).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(3).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(2).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(1)) + ",SIGNIN_SUCCESS,Jarvi.Melendez");

        String ip = null;
        for (String line : lines) {
            ip = signInHackerDetectorTest.parseLine(line);
            if (ip != null) {
                break;
            }
        }

        assertNull(ip);
    }

    @Test
    @Order(5)
    public void testParseLine_whenSignInFailure4AndSuccess1Failure2TimesIn5MinInSameIp_thenNull() {
        List<String> lines = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        lines.add("80.238.9.179," + toMilli(now.minusMinutes(5).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(4).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(3).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(2).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(1)) + ",SIGNIN_SUCCESS,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(1).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(1).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");


        String ip = null;
        for (String line : lines) {
            ip = signInHackerDetectorTest.parseLine(line);
            if (ip != null) {
                break;
            }
        }

        assertNull(ip);
    }

    @Test
    @Order(6)
    public void testParseLine_whenSignInFailure10TimesIn5MinInMultiplesIp_thenNull() {
        List<String> lines = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        lines.add("80.238.9.179," + toMilli(now.minusMinutes(5).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.180," + toMilli(now.minusMinutes(5).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(4).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(4).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.160," + toMilli(now.minusMinutes(3).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(3).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.150," + toMilli(now.minusMinutes(2).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.159," + toMilli(now.minusMinutes(2).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.190," + toMilli(now.minusMinutes(1).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.159," + toMilli(now.minusMinutes(1).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");

        String ip = null;
        for (String line : lines) {
            ip = signInHackerDetectorTest.parseLine(line);
            if (ip != null) {
                break;
            }
        }

        assertNull(ip);
    }

    @Test
    @Order(7)
    public void testParseLine_whenSignInFailure10TimesIn5MinInMultiplesIpAnd5TimeSameIp_thenIp() {
        List<String> lines = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        lines.add("80.238.9.179," + toMilli(now.minusMinutes(5).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.180," + toMilli(now.minusMinutes(5).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(4).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(4).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.160," + toMilli(now.minusMinutes(3).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(3).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.150," + toMilli(now.minusMinutes(2).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.159," + toMilli(now.minusMinutes(2).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.190," + toMilli(now.minusMinutes(1).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(1).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");

        String ip = null;
        for (String line : lines) {
            ip = signInHackerDetectorTest.parseLine(line);
            if (ip != null) {
                break;
            }
        }

        assertEquals("80.238.9.179", ip);
    }

    @Test
    @Order(8)
    public void testParseLine_whenSignInFailure5TimesIn10MinInSameIp_thenIp() {
        List<String> lines = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        lines.add("80.238.9.179," + toMilli(now.minusMinutes(10).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(8).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(5).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(3).minusSeconds(30)) + ",SIGNIN_FAILURE,Jarvi.Melendez");
        lines.add("80.238.9.179," + toMilli(now.minusMinutes(1).minusSeconds(10)) + ",SIGNIN_FAILURE,Jarvi.Melendez");

        String ip = null;
        for (String line : lines) {
            ip = signInHackerDetectorTest.parseLine(line);
            if (ip != null) {
                break;
            }
        }

        assertNull(ip);
    }

    private long toMilli(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


}
