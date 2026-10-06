package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.ProductServiceImpl;

import java.io.IOException;

@WebServlet("/product")
public class ProductListController extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long totalProduct = productService.count();
        int totalPage = (int) Math.ceil((double) totalProduct / PAGE_SIZE);
        int maxPage = Math.max(totalPage, 1);

        int page = parsePage(req.getParameter("page"));
        if (page > maxPage) {
            page = maxPage;
        }

        req.setAttribute("products", productService.findPagination(page, PAGE_SIZE));
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPage", totalPage);

        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/views/client/product.jsp").include(req, resp);
    }

    private int parsePage(String value) {
        if (value == null || value.isBlank()) {
            return 1;
        }
        try {
            int page = Integer.parseInt(value);
            return Math.max(page, 1);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
