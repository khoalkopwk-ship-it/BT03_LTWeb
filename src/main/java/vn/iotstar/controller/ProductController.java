package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.CategoryServiceImpl;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.ProductServiceImpl;
import vn.iotstar.util.CsrfUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@WebServlet(urlPatterns = {
        "/admin/product",
        "/admin/product/add",
        "/admin/product/edit",
        "/admin/product/delete"
})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class ProductController extends HttpServlet {
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("jpg", "jpeg", "png", "webp", "gif");

    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    private boolean checkAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) session.getAttribute("account");
        return user != null && user.getRoleid() == 1;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        HttpSession session = req.getSession();
        req.setAttribute("csrfToken", CsrfUtil.getOrCreateToken(session));
        moveFlashToRequest(session, req);

        String path = req.getServletPath();
        switch (path) {
            case "/admin/product":
                req.setAttribute("products", productService.findAll());
                render(req, resp, "/views/admin/product/list-product.jsp");
                break;

            case "/admin/product/add":
                req.setAttribute("categories", categoryService.findAll());
                render(req, resp, "/views/admin/product/add-product.jsp");
                break;

            case "/admin/product/edit":
                Integer id = parsePositiveInt(req.getParameter("id"));
                if (id == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id không hợp lệ");
                    return;
                }
                Product product = productService.findById(id);
                if (product == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
                    return;
                }
                req.setAttribute("product", product);
                req.setAttribute("categories", categoryService.findAll());
                render(req, resp, "/views/admin/product/edit-product.jsp");
                break;

            case "/admin/product/delete":
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Dùng POST để xóa sản phẩm");
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setCharacterEncoding("UTF-8");
        boolean csrfValid;
        try {
            csrfValid = CsrfUtil.isValid(req);
        } catch (IllegalStateException e) {
            resp.sendError(413, "Request/upload vượt quá giới hạn cho phép");
            return;
        }
        if (!csrfValid) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token không hợp lệ");
            return;
        }

        String path = req.getServletPath();
        switch (path) {
            case "/admin/product/add":
                handleAdd(req, resp);
                break;
            case "/admin/product/edit":
                handleEdit(req, resp);
                break;
            case "/admin/product/delete":
                handleDelete(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ProductForm form = parseForm(req);
        if (form.error != null) {
            renderAddWithError(req, resp, form.error);
            return;
        }

        String image;
        try {
            image = uploadImage(req);
        } catch (IllegalArgumentException | IllegalStateException e) {
            renderAddWithError(req, resp, e.getMessage() == null ? "Upload ảnh không hợp lệ" : e.getMessage());
            return;
        }

        Product product = new Product();
        product.setProduct_name(form.name);
        product.setPrice(form.price);
        product.setDescription(form.description);
        product.setCategory(form.category);
        product.setImage(image);
        productService.insert(product);

        flash(req.getSession(), "success", "Thêm sản phẩm thành công");
        resp.sendRedirect(req.getContextPath() + "/admin/product");
    }

    private void handleEdit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer id = parsePositiveInt(req.getParameter("product_id"));
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id không hợp lệ");
            return;
        }
        Product product = productService.findById(id);
        if (product == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
            return;
        }

        ProductForm form = parseForm(req);
        if (form.error != null) {
            renderEditWithError(req, resp, product, form.error);
            return;
        }

        String newImage;
        try {
            newImage = uploadImage(req);
        } catch (IllegalArgumentException | IllegalStateException e) {
            renderEditWithError(req, resp, product, e.getMessage() == null ? "Upload ảnh không hợp lệ" : e.getMessage());
            return;
        }

        product.setProduct_name(form.name);
        product.setPrice(form.price);
        product.setDescription(form.description);
        product.setCategory(form.category);
        if (newImage != null) {
            product.setImage(newImage);
        }
        productService.update(product);

        flash(req.getSession(), "success", "Cập nhật sản phẩm thành công");
        resp.sendRedirect(req.getContextPath() + "/admin/product");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = parsePositiveInt(req.getParameter("id"));
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id không hợp lệ");
            return;
        }
        if (productService.findById(id) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
            return;
        }
        productService.delete(id);
        flash(req.getSession(), "success", "Xóa sản phẩm thành công");
        resp.sendRedirect(req.getContextPath() + "/admin/product");
    }

    private ProductForm parseForm(HttpServletRequest req) {
        String name = trim(req.getParameter("product_name"));
        String description = trim(req.getParameter("description"));
        if (name == null || name.isEmpty() || name.length() > 255) {
            return ProductForm.error("Tên sản phẩm không hợp lệ");
        }

        BigDecimal price;
        try {
            price = new BigDecimal(req.getParameter("price"));
        } catch (Exception e) {
            return ProductForm.error("Giá sản phẩm không hợp lệ");
        }
        int integerDigits = price.precision() - price.scale();
        if (price.compareTo(BigDecimal.ZERO) <= 0 || price.scale() > 2 || integerDigits > 16) {
            return ProductForm.error("Giá phải lớn hơn 0, tối đa 16 chữ số phần nguyên và 2 chữ số thập phân");
        }

        Integer categoryId = parsePositiveInt(req.getParameter("cate_id"));
        if (categoryId == null) {
            return ProductForm.error("Category không hợp lệ");
        }
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            return ProductForm.error("Category không tồn tại");
        }

        return new ProductForm(name, price, description, category, null);
    }

    private String uploadImage(HttpServletRequest req)
            throws IOException, ServletException {
        Part part = req.getPart("image");
        if (part == null || part.getSize() == 0) {
            return null;
        }
        if (part.getSize() > 5L * 1024 * 1024) {
            throw new IllegalArgumentException("Ảnh không được vượt quá 5 MB");
        }

        String submittedFileName = part.getSubmittedFileName();
        if (submittedFileName == null || submittedFileName.isBlank()) {
            throw new IllegalArgumentException("Tên file ảnh không hợp lệ");
        }
        String originalName = Paths.get(submittedFileName)
                .getFileName()
                .toString();
        int dot = originalName.lastIndexOf('.');
        if (dot < 0 || dot == originalName.length() - 1) {
            throw new IllegalArgumentException("File ảnh phải có phần mở rộng hợp lệ");
        }
        String extension = originalName.substring(dot + 1).toLowerCase(Locale.ROOT);
        String contentType = part.getContentType();
        if (!ALLOWED_EXTENSIONS.contains(extension)
                || contentType == null
                || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new IllegalArgumentException("Chỉ chấp nhận ảnh JPG, JPEG, PNG, WEBP hoặc GIF");
        }

        String uploadPath = req.getServletContext().getRealPath("/uploads/products");
        if (uploadPath == null) {
            throw new IOException("Không xác định được thư mục uploads/products của ứng dụng");
        }
        Path uploadDir = Path.of(uploadPath);
        Files.createDirectories(uploadDir);

        String storedName = UUID.randomUUID() + "." + extension;
        Path target = uploadDir.resolve(storedName).normalize();
        if (!target.getParent().equals(uploadDir.normalize())) {
            throw new IllegalArgumentException("Tên file ảnh không hợp lệ");
        }
        try (var input = part.getInputStream()) {
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return storedName;
    }

    private void renderAddWithError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("message", message);
        req.setAttribute("categories", categoryService.findAll());
        req.setAttribute("csrfToken", CsrfUtil.getOrCreateToken(req.getSession()));
        render(req, resp, "/views/admin/product/add-product.jsp");
    }

    private void renderEditWithError(HttpServletRequest req, HttpServletResponse resp, Product product, String message)
            throws ServletException, IOException {
        req.setAttribute("message", message);
        req.setAttribute("product", product);
        req.setAttribute("categories", categoryService.findAll());
        req.setAttribute("csrfToken", CsrfUtil.getOrCreateToken(req.getSession()));
        render(req, resp, "/views/admin/product/edit-product.jsp");
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher(jsp).include(req, resp);
    }

    private Integer parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private void flash(HttpSession session, String type, String message) {
        session.setAttribute("flashType", type);
        session.setAttribute("flashMessage", message);
    }

    private void moveFlashToRequest(HttpSession session, HttpServletRequest req) {
        Object message = session.getAttribute("flashMessage");
        if (message != null) {
            req.setAttribute("flashMessage", message);
            req.setAttribute("flashType", session.getAttribute("flashType"));
            session.removeAttribute("flashMessage");
            session.removeAttribute("flashType");
        }
    }

    private static class ProductForm {
        private final String name;
        private final BigDecimal price;
        private final String description;
        private final Category category;
        private final String error;

        private ProductForm(String name, BigDecimal price, String description, Category category, String error) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.category = category;
            this.error = error;
        }

        private static ProductForm error(String message) {
            return new ProductForm(null, null, null, null, message);
        }
    }
}
