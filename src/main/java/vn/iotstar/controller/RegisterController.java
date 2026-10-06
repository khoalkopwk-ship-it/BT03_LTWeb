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
import vn.iotstar.util.PasswordUtil;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private final UserService userService = new UserServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = trim(req.getParameter("email"));
        String username = trim(req.getParameter("username"));
        String fullname = trim(req.getParameter("fullname"));
        String password = req.getParameter("password");

        String validationError = validate(email, username, fullname, password);
        if (validationError != null) {
            req.setAttribute("message", validationError);
            preserveForm(req, email, username, fullname);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        User existingEmail = userService.findByEmail(email);
        if (existingEmail != null && existingEmail.getStatus() == 1) {
            req.setAttribute("message", "Email đã tồn tại");
            preserveForm(req, email, username, fullname);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        User existingUsername = userService.findByUsername(username);
        if (existingUsername != null
                && (existingEmail == null || existingUsername.getId() != existingEmail.getId())) {
            req.setAttribute("message", "Username đã tồn tại");
            preserveForm(req, email, username, fullname);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        String otp = OtpUtil.generateSixDigitOtp();
        if (!EmailUtil.sendOTP(email, otp)) {
            req.setAttribute(
                    "message",
                    "Không thể gửi OTP. Hãy kiểm tra cấu hình SMTP_EMAIL/SMTP_APP_PASSWORD rồi thử lại."
            );
            preserveForm(req, email, username, fullname);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            User user = existingEmail != null ? existingEmail : new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setFullname(fullname);
            user.setPassword(PasswordUtil.hash(password));
            user.setOtp(otp);
            user.setStatus(0);
            // Self-registration must never inherit elevated privileges from a pending record.
            user.setRoleid(5);

            if (existingEmail == null) {
                userService.register(user);
            } else {
                userService.update(user);
            }
        } catch (RuntimeException e) {
            req.setAttribute("message", "Không thể tạo tài khoản. Email hoặc username có thể đã được sử dụng.");
            preserveForm(req, email, username, fullname);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("otpEmail", email);
        session.setAttribute("otpExpiresAt", System.currentTimeMillis() + OtpUtil.OTP_TTL_MILLIS);
        session.setAttribute("otpAttempts", 0);
        resp.sendRedirect(req.getContextPath() + "/verify-otp");
    }

    private String validate(String email, String username, String fullname, String password) {
        if (email == null || username == null || fullname == null || password == null) {
            return "Vui lòng nhập đầy đủ thông tin";
        }
        if (email.length() > 255 || !EMAIL_PATTERN.matcher(email).matches()) {
            return "Email không hợp lệ";
        }
        if (username.length() < 3 || username.length() > 100) {
            return "Username phải từ 3 đến 100 ký tự";
        }
        if (fullname.isEmpty() || fullname.length() > 255) {
            return "Fullname không hợp lệ";
        }
        if (password.length() < 6 || password.length() > 72) {
            return "Password phải từ 6 đến 72 ký tự";
        }
        return null;
    }

    private void preserveForm(HttpServletRequest req, String email, String username, String fullname) {
        req.setAttribute("email", email);
        req.setAttribute("username", username);
        req.setAttribute("fullname", fullname);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
