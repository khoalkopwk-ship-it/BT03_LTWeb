package vn.iotstar.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;


@WebServlet("/logout")
public class LogoutController extends HttpServlet {


    // Xóa session đăng nhập
    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {


        HttpSession session =
                req.getSession(false);



        if(session != null){

            session.invalidate();

        }



        resp.sendRedirect(
                req.getContextPath()
                        + "/login"
        );

    }

}