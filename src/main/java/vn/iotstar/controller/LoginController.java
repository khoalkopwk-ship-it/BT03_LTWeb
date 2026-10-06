package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.UserServiceImpl;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession(false) != null
                && req.getSession(false).getAttribute("account") != null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");

        if (username == null || password == null || password.isEmpty()) {
            req.setAttribute("message", "Vui lòng nhập username và password");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        User user = userService.login(username, password);
        if (user == null) {
            req.setAttribute("message", "Sai username hoặc password");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        if (user.getStatus() != 1) {
            req.setAttribute("message", "Tài khoản chưa được kích hoạt");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        req.changeSessionId();
        session.setAttribute("account", user);
        handleRememberCookie(req, resp, username);
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    private void handleRememberCookie(HttpServletRequest req, HttpServletResponse resp, String username) {
        String contextPath = req.getContextPath().isEmpty() ? "/" : req.getContextPath();
        boolean remember = req.getParameter("remember") != null;

        Cookie usernameCookie = new Cookie("username", remember ? username : "");
        usernameCookie.setPath(contextPath);
        usernameCookie.setHttpOnly(true);
        usernameCookie.setSecure(req.isSecure());
        usernameCookie.setAttribute("SameSite", "Lax");
        usernameCookie.setMaxAge(remember ? 7 * 24 * 60 * 60 : 0);
        resp.addCookie(usernameCookie);

        // Always remove the legacy cookie that stored the password in plain text.
        Cookie legacyPasswordCookie = new Cookie("password", "");
        legacyPasswordCookie.setPath(contextPath);
        legacyPasswordCookie.setHttpOnly(true);
        legacyPasswordCookie.setSecure(req.isSecure());
        legacyPasswordCookie.setAttribute("SameSite", "Lax");
        legacyPasswordCookie.setMaxAge(0);
        resp.addCookie(legacyPasswordCookie);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
