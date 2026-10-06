package vn.iotstar.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public final class EmailUtil {
    private EmailUtil() {
    }
    /**
     * Sends an OTP using SMTP credentials from environment variables or JVM properties.
     * Environment variables: SMTP_EMAIL, SMTP_APP_PASSWORD.
     * JVM properties: smtp.email, smtp.appPassword.
     */
    public static boolean sendOTP(String toEmail, String otp) {
        String fromEmail = firstNonBlank(
                System.getenv("SMTP_EMAIL"),
                System.getProperty("smtp.email")
        );
        String password = firstNonBlank(
                System.getenv("SMTP_APP_PASSWORD"),
                System.getProperty("smtp.appPassword")
        );
        if (fromEmail == null || password == null) {
            System.err.println("Email OTP is not configured. Set SMTP_EMAIL and SMTP_APP_PASSWORD.");
            return false;
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        Session session = Session.getInstance(
                props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                }
        );
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            message.setSubject("Xác nhận tài khoản / OTP", "UTF-8");
            message.setText("Mã OTP của bạn là: " + otp, "UTF-8");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("Không thể gửi OTP tới " + toEmail + ": " + e.getMessage());
            return false;
        }
    }
    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }
}
