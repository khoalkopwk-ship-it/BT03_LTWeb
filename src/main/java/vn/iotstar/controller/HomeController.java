package vn.iotstar.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.ProductService;
import vn.iotstar.service.ProductServiceImpl;


import java.io.IOException;



@WebServlet("/home")
public class HomeController extends HttpServlet {


    private ProductService productService =
            new ProductServiceImpl();



    // Hiển thị trang chủ
    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {


        req.setAttribute(
                "products",
                productService.findTop10NewProduct()
        );


        // Keep the response open for SiteMesh decoration on Tomcat 11.
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher(
                "/views/client/home.jsp"
        ).include(req, resp);
        return;

    }


}
