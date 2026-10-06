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
import vn.iotstar.util.PasswordUtil;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!hasValidResetSession(session)) {
            if (session != null) {
                clearResetSession(session);
            }
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        if (!hasValidResetSession(session)) {
            if (session != null) {
                clearResetSession(session);
            }
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String password = req.getParameter("password");
        if (password == null || password.length() < 6 || password.length() > 72) {
            req.setAttribute("message", "Password phải từ 6 đến 72 ký tự");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }

        String email = (String) session.getAttribute("resetEmail");
        User user = userService.findByEmail(email);
        if (user == null) {
            clearResetSession(session);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy tài khoản");
            return;
        }

        user.setPassword(PasswordUtil.hash(password));
        user.setOtp(null);
        userService.update(user);
        clearResetSession(session);

        req.setAttribute("message", "Đổi mật khẩu thành công");
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    private boolean hasValidResetSession(HttpSession session) {
        if (session == null || session.getAttribute("resetEmail") == null) {
            return false;
        }
        Long expiresAt = (Long) session.getAttribute("resetExpiresAt");
        return !OtpUtil.isExpired(expiresAt);
    }

    private void clearResetSession(HttpSession session) {
        session.removeAttribute("resetEmail");
        session.removeAttribute("resetExpiresAt");
    }
}
