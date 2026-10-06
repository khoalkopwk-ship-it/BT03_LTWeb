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

@WebServlet("/verify-otp")
public class VerifyOTPController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("otpEmail") == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String otpInput = trim(req.getParameter("otp"));
        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String email = (String) session.getAttribute("otpEmail");
        Long expiresAt = (Long) session.getAttribute("otpExpiresAt");
        Integer attempts = (Integer) session.getAttribute("otpAttempts");
        attempts = attempts == null ? 0 : attempts;

        if (email == null || OtpUtil.isExpired(expiresAt)) {
            clearRegistrationOtpSession(session);
            req.setAttribute("message", "OTP đã hết hạn. Vui lòng đăng ký lại để nhận OTP mới.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        if (attempts >= OtpUtil.MAX_ATTEMPTS) {
            clearRegistrationOtpSession(session);
            req.setAttribute("message", "Bạn đã nhập sai OTP quá nhiều lần. Vui lòng đăng ký lại.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        User user = userService.findByEmail(email);
        if (user != null && user.getOtp() != null && user.getOtp().equals(otpInput)) {
            user.setStatus(1);
            user.setOtp(null);
            userService.update(user);
            clearRegistrationOtpSession(session);
            req.setAttribute("message", "Kích hoạt tài khoản thành công");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        session.setAttribute("otpAttempts", attempts + 1);
        req.setAttribute("message", "OTP không chính xác");
        req.getRequestDispatcher("/views/verify-otp.jsp").forward(req, resp);
    }

    private void clearRegistrationOtpSession(HttpSession session) {
        session.removeAttribute("otpEmail");
        session.removeAttribute("otpExpiresAt");
        session.removeAttribute("otpAttempts");
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
