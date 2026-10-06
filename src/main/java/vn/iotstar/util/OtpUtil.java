package vn.iotstar.util;

import java.security.SecureRandom;

public final class OtpUtil {
    public static final long OTP_TTL_MILLIS = 10 * 60 * 1000L;
    public static final int MAX_ATTEMPTS = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private OtpUtil() {
    }

    public static String generateSixDigitOtp() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

    public static boolean isExpired(Long expiresAt) {
        return expiresAt == null || System.currentTimeMillis() > expiresAt;
    }
}
