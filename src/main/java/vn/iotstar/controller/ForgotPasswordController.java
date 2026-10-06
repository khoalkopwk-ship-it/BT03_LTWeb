package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.UserServiceImpl;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.OtpUtil;

import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = trim(req.getParameter("email"));
        if (email == null || email.isEmpty()) {
            req.setAttribute("message", "Vui lòng nhập email");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            req.setAttribute("message", "Email chưa được đăng ký");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        String otp = OtpUtil.generateSixDigitOtp();
        if (!EmailUtil.sendOTP(email, otp)) {
            req.setAttribute(
                    "message",
                    "Không thể gửi OTP. Hãy kiểm tra cấu hình SMTP_EMAIL/SMTP_APP_PASSWORD rồi thử lại."
            );
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        user.setOtp(otp);
        userService.update(user);

        HttpSession session = req.getSession();
        session.setAttribute("forgotEmail", email);
        session.setAttribute("forgotOtpExpiresAt", System.currentTimeMillis() + OtpUtil.OTP_TTL_MILLIS);
        session.setAttribute("forgotOtpAttempts", 0);
        resp.sendRedirect(req.getContextPath() + "/verify-forgot-otp");
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
