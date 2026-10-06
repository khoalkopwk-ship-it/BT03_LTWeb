package vn.iotstar.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.UserServiceImpl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@WebServlet("/profile")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024
)
public class ProfileController extends HttpServlet {

    private UserService userService =
            new UserServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    )
            throws ServletException, IOException {


        HttpSession session =
                req.getSession();


        User account =
                (User) session.getAttribute("account");


        if(account == null){

            resp.sendRedirect(
                    req.getContextPath()
                            + "/login"
            );

            return;
        }


        req.setAttribute(
                "user",
                account
        );
        req.getRequestDispatcher(
                        "/views/profile.jsp"
                )
                .forward(req,resp);

    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    )
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session =
                req.getSession();
        User account =
                (User) session.getAttribute("account");
        if(account == null){
            resp.sendRedirect(
                    req.getContextPath()
                            + "/login"
            );
            return;
        }
        String fullname =
                req.getParameter("fullname");
        String phone =
                req.getParameter("phone");
        account.setFullname(fullname);
        account.setPhone(phone);
        Part part =
                req.getPart("avatar");
        if(part != null
                && part.getSize()>0){
            String fileName =
                    UUID.randomUUID()
                            +"_"
                            +part.getSubmittedFileName();
            String uploadPath =
                    getServletContext()
                            .getRealPath(
                                    "/uploads/avatar"
                            );
            File folder =
                    new File(uploadPath);
            if(!folder.exists()){

                folder.mkdirs();
            }
            part.write(
                    uploadPath
                            + File.separator
                            + fileName
            );
            account.setAvatar(
                    fileName
            );
        }
        userService.update(account);

        /*
          update session
        */
        session.setAttribute(
                "account",
                account
        );
        resp.sendRedirect(
                req.getContextPath()
                        +"/profile"
        );
    }
}