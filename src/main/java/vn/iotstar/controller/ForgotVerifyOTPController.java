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
import vn.iotstar.util.OtpUtil;

import java.io.IOException;

@WebServlet("/verify-forgot-otp")
public class ForgotVerifyOTPController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("forgotEmail") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        req.getRequestDispatcher("/views/verify-forgot-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String otpInput = trim(req.getParameter("otp"));
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String email = (String) session.getAttribute("forgotEmail");
        Long expiresAt = (Long) session.getAttribute("forgotOtpExpiresAt");
        Integer attempts = (Integer) session.getAttribute("forgotOtpAttempts");
        attempts = attempts == null ? 0 : attempts;

        if (email == null || OtpUtil.isExpired(expiresAt)) {
            clearForgotOtpSession(session);
            req.setAttribute("message", "OTP đã hết hạn. Vui lòng yêu cầu OTP mới.");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        if (attempts >= OtpUtil.MAX_ATTEMPTS) {
            clearForgotOtpSession(session);
            req.setAttribute("message", "Bạn đã nhập sai OTP quá nhiều lần. Vui lòng yêu cầu OTP mới.");
            req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        User user = userService.findByEmail(email);
        if (user != null && user.getOtp() != null && user.getOtp().equals(otpInput)) {
            clearForgotOtpSession(session);
            session.setAttribute("resetEmail", email);
            session.setAttribute("resetExpiresAt", System.currentTimeMillis() + OtpUtil.OTP_TTL_MILLIS);
            resp.sendRedirect(req.getContextPath() + "/reset-password");
            return;
        }

        session.setAttribute("forgotOtpAttempts", attempts + 1);
        req.setAttribute("message", "OTP không chính xác");
        req.getRequestDispatcher("/views/verify-forgot-otp.jsp").forward(req, resp);
    }

    private void clearForgotOtpSession(HttpSession session) {
        session.removeAttribute("forgotEmail");
        session.removeAttribute("forgotOtpExpiresAt");
        session.removeAttribute("forgotOtpAttempts");
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
