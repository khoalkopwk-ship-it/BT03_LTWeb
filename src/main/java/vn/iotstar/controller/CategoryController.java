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
import vn.iotstar.entity.User;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.CategoryServiceImpl;
import vn.iotstar.util.CsrfUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@WebServlet(urlPatterns = {
        "/admin/category",
        "/admin/category/add",
        "/admin/category/edit",
        "/admin/category/delete"
})
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class CategoryController extends HttpServlet {

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("jpg", "jpeg", "png", "webp", "gif");

    private final CategoryService categoryService =
            new CategoryServiceImpl();

    /*
     * Kiểm tra quyền Admin.
     * Hiện tại vẫn giữ phân quyền như project đang chạy.
     */
    private boolean checkAdmin(HttpServletRequest req) {

        HttpSession session = req.getSession(false);

        if (session == null) {
            return false;
        }

        User user =
                (User) session.getAttribute("account");

        return user != null
                && user.getRoleid() == 1;
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        if (!checkAdmin(req)) {

            resp.sendRedirect(
                    req.getContextPath() + "/login"
            );

            return;
        }

        HttpSession session =
                req.getSession();

        req.setAttribute(
                "csrfToken",
                CsrfUtil.getOrCreateToken(session)
        );

        moveFlashToRequest(session, req);

        String path =
                req.getServletPath();

        switch (path) {

            /*
             * LIST CATEGORY
             */
            case "/admin/category":

                req.setAttribute(
                        "categories",
                        categoryService.findAll()
                );

                render(
                        req,
                        resp,
                        "/views/admin/category/list-category.jsp"
                );

                break;

            /*
             * FORM ADD CATEGORY
             */
            case "/admin/category/add":

                render(
                        req,
                        resp,
                        "/views/admin/category/add-category.jsp"
                );

                break;

            /*
             * FORM EDIT CATEGORY
             */
            case "/admin/category/edit":

                Integer id =
                        parsePositiveInt(
                                req.getParameter("id")
                        );

                if (id == null) {

                    resp.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Category id không hợp lệ"
                    );

                    return;
                }

                Category category =
                        categoryService.findById(id);

                if (category == null) {

                    resp.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Không tìm thấy category"
                    );

                    return;
                }

                req.setAttribute(
                        "category",
                        category
                );

                render(
                        req,
                        resp,
                        "/views/admin/category/edit-category.jsp"
                );

                break;

            /*
             * Không cho DELETE bằng GET
             */
            case "/admin/category/delete":

                resp.sendError(
                        HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                        "Dùng POST để xóa category"
                );

                break;

            default:

                resp.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        if (!checkAdmin(req)) {

            resp.sendRedirect(
                    req.getContextPath() + "/login"
            );

            return;
        }

        /*
         * Phải set UTF-8 trước khi đọc parameter
         * của multipart/form-data.
         */
        req.setCharacterEncoding("UTF-8");

        boolean csrfValid;

        try {

            csrfValid =
                    CsrfUtil.isValid(req);

        } catch (IllegalStateException e) {

            resp.sendError(
                    413,
                    "Request/upload vượt quá giới hạn cho phép"
            );

            return;
        }

        if (!csrfValid) {

            resp.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "CSRF token không hợp lệ"
            );

            return;
        }

        String path =
                req.getServletPath();

        switch (path) {

            case "/admin/category/add":

                handleAdd(req, resp);

                break;

            case "/admin/category/edit":

                handleEdit(req, resp);

                break;

            case "/admin/category/delete":

                handleDelete(req, resp);

                break;

            default:

                resp.sendError(
                        HttpServletResponse.SC_METHOD_NOT_ALLOWED
                );
        }
    }

    /*
     * ==============================
     * ADD CATEGORY
     * ==============================
     */
    private void handleAdd(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        String name =
                trim(
                        req.getParameter("cate_name")
                );

        /*
         * Validate tên Category
         */
        if (name == null
                || name.isEmpty()
                || name.length() > 255) {

            req.setAttribute(
                    "message",
                    "Tên Category không hợp lệ"
            );

            req.setAttribute(
                    "cate_name",
                    name
            );

            req.setAttribute(
                    "csrfToken",
                    CsrfUtil.getOrCreateToken(
                            req.getSession()
                    )
            );

            render(
                    req,
                    resp,
                    "/views/admin/category/add-category.jsp"
            );

            return;
        }

        String icon;

        try {

            icon =
                    uploadIcon(req);

        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            req.setAttribute(
                    "message",
                    e.getMessage() == null
                            ? "Upload ảnh không hợp lệ"
                            : e.getMessage()
            );

            req.setAttribute(
                    "cate_name",
                    name
            );

            req.setAttribute(
                    "csrfToken",
                    CsrfUtil.getOrCreateToken(
                            req.getSession()
                    )
            );

            render(
                    req,
                    resp,
                    "/views/admin/category/add-category.jsp"
            );

            return;
        }

        Category category =
                new Category();

        category.setCate_name(name);
        category.setIcons(icon);

        categoryService.insert(category);

        flash(
                req.getSession(),
                "success",
                "Thêm Category thành công"
        );

        resp.sendRedirect(
                req.getContextPath()
                        + "/admin/category"
        );
    }

    /*
     * ==============================
     * EDIT CATEGORY
     * ==============================
     */
    private void handleEdit(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        Integer id =
                parsePositiveInt(
                        req.getParameter("cate_id")
                );

        if (id == null) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Category id không hợp lệ"
            );

            return;
        }

        Category category =
                categoryService.findById(id);

        if (category == null) {

            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy Category"
            );

            return;
        }

        String name =
                trim(
                        req.getParameter("cate_name")
                );

        /*
         * Validate tên
         */
        if (name == null
                || name.isEmpty()
                || name.length() > 255) {

            req.setAttribute(
                    "message",
                    "Tên Category không hợp lệ"
            );

            req.setAttribute(
                    "category",
                    category
            );

            req.setAttribute(
                    "csrfToken",
                    CsrfUtil.getOrCreateToken(
                            req.getSession()
                    )
            );

            render(
                    req,
                    resp,
                    "/views/admin/category/edit-category.jsp"
            );

            return;
        }

        String newIcon;

        try {

            newIcon =
                    uploadIcon(req);

        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            req.setAttribute(
                    "message",
                    e.getMessage() == null
                            ? "Upload ảnh không hợp lệ"
                            : e.getMessage()
            );

            req.setAttribute(
                    "category",
                    category
            );

            req.setAttribute(
                    "csrfToken",
                    CsrfUtil.getOrCreateToken(
                            req.getSession()
                    )
            );

            render(
                    req,
                    resp,
                    "/views/admin/category/edit-category.jsp"
            );

            return;
        }

        /*
         * Update tên
         */
        category.setCate_name(name);

        /*
         * Nếu user chọn ảnh mới
         * thì update ảnh.
         *
         * Nếu không chọn ảnh mới
         * thì giữ nguyên ảnh cũ.
         */
        if (newIcon != null) {

            category.setIcons(newIcon);
        }

        categoryService.update(category);

        flash(
                req.getSession(),
                "success",
                "Cập nhật Category thành công"
        );

        resp.sendRedirect(
                req.getContextPath()
                        + "/admin/category"
        );
    }

    /*
     * ==============================
     * DELETE CATEGORY
     * ==============================
     */
    private void handleDelete(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {

        Integer id =
                parsePositiveInt(
                        req.getParameter("id")
                );

        if (id == null) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Category id không hợp lệ"
            );

            return;
        }

        Category category =
                categoryService.findById(id);

        if (category == null) {

            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy Category"
            );

            return;
        }

        /*
         * Không được xóa Category
         * nếu đang có Product sử dụng.
         */
        if (categoryService.hasProducts(id)) {

            flash(
                    req.getSession(),
                    "error",
                    "Không thể xóa Category đang có sản phẩm"
            );

            resp.sendRedirect(
                    req.getContextPath()
                            + "/admin/category"
            );

            return;
        }

        categoryService.delete(id);

        flash(
                req.getSession(),
                "success",
                "Xóa Category thành công"
        );

        resp.sendRedirect(
                req.getContextPath()
                        + "/admin/category"
        );
    }

    /*
     * ==============================
     * UPLOAD CATEGORY IMAGE
     * ==============================
     */
    private String uploadIcon(
            HttpServletRequest req
    ) throws IOException, ServletException {

        Part part =
                req.getPart("icons");

        /*
         * Không chọn ảnh
         */
        if (part == null
                || part.getSize() == 0) {

            return null;
        }

        /*
         * Max 5 MB
         */
        if (part.getSize()
                > 5L * 1024 * 1024) {

            throw new IllegalArgumentException(
                    "Ảnh không được vượt quá 5 MB"
            );
        }

        String submittedFileName =
                part.getSubmittedFileName();

        if (submittedFileName == null
                || submittedFileName.isBlank()) {

            throw new IllegalArgumentException(
                    "Tên file ảnh không hợp lệ"
            );
        }

        /*
         * Chỉ lấy filename,
         * loại bỏ đường dẫn client gửi lên.
         */
        String originalName =
                Paths.get(
                                submittedFileName
                        )
                        .getFileName()
                        .toString();

        int dot =
                originalName.lastIndexOf('.');

        if (dot < 0
                || dot
                == originalName.length() - 1) {

            throw new IllegalArgumentException(
                    "File ảnh phải có phần mở rộng hợp lệ"
            );
        }

        String extension =
                originalName
                        .substring(dot + 1)
                        .toLowerCase(
                                Locale.ROOT
                        );

        String contentType =
                part.getContentType();

        /*
         * Kiểm tra extension
         * + MIME type.
         */
        if (!ALLOWED_EXTENSIONS.contains(extension)
                || contentType == null
                || !contentType
                .toLowerCase(Locale.ROOT)
                .startsWith("image/")) {

            throw new IllegalArgumentException(
                    "Chỉ chấp nhận ảnh JPG, JPEG, PNG, WEBP hoặc GIF"
            );
        }

        /*
         * Thư mục lưu Category image
         */
        String uploadPath =
                req.getServletContext()
                        .getRealPath(
                                "/uploads/categories"
                        );

        if (uploadPath == null) {

            throw new IOException(
                    "Không xác định được thư mục uploads/categories"
            );
        }

        Path uploadDir =
                Path.of(uploadPath);

        Files.createDirectories(
                uploadDir
        );

        /*
         * Dùng UUID tránh trùng filename.
         */
        String storedName =
                UUID.randomUUID()
                        + "."
                        + extension;

        Path target =
                uploadDir
                        .resolve(storedName)
                        .normalize();

        /*
         * Chặn path traversal.
         */
        if (!target.getParent()
                .equals(
                        uploadDir.normalize()
                )) {

            throw new IllegalArgumentException(
                    "Tên file ảnh không hợp lệ"
            );
        }

        /*
         * Ghi file
         */
        try (var input =
                     part.getInputStream()) {

            Files.copy(
                    input,
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }

        /*
         * Database chỉ lưu filename:
         *
         * abc-xyz-123.jpg
         */
        return storedName;
    }

    /*
     * ==============================
     * RENDER JSP
     * ==============================
     */
    private void render(
            HttpServletRequest req,
            HttpServletResponse resp,
            String jsp
    ) throws ServletException, IOException {

        /*
         * SiteMesh 3 + Tomcat 11:
         * dùng include thay vì forward.
         */
        resp.setContentType(
                "text/html;charset=UTF-8"
        );

        req.getRequestDispatcher(jsp)
                .include(req, resp);
    }

    /*
     * Parse ID > 0
     */
    private Integer parsePositiveInt(
            String value
    ) {

        try {

            int parsed =
                    Integer.parseInt(value);

            return parsed > 0
                    ? parsed
                    : null;

        } catch (Exception e) {

            return null;
        }
    }

    /*
     * Trim String
     */
    private String trim(
            String value
    ) {

        if (value == null) {
            return null;
        }

        return value.trim();
    }

    /*
     * Flash message
     */
    private void flash(
            HttpSession session,
            String type,
            String message
    ) {

        session.setAttribute(
                "flashType",
                type
        );

        session.setAttribute(
                "flashMessage",
                message
        );
    }

    /*
     * Đưa flash message
     * từ Session → Request.
     */
    private void moveFlashToRequest(
            HttpSession session,
            HttpServletRequest req
    ) {

        Object message =
                session.getAttribute(
                        "flashMessage"
                );

        if (message != null) {

            req.setAttribute(
                    "flashMessage",
                    message
            );

            req.setAttribute(
                    "flashType",
                    session.getAttribute(
                            "flashType"
                    )
            );

            session.removeAttribute(
                    "flashMessage"
            );

            session.removeAttribute(
                    "flashType"
            );
        }
    }
}