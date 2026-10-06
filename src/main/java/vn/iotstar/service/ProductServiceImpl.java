package vn.iotstar.service;

import vn.iotstar.dao.ProductDAO;
import vn.iotstar.dao.ProductDAOImpl;
import vn.iotstar.entity.Product;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private ProductDAO productDAO =
            new ProductDAOImpl();
    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }
    @Override
    public Product findById(int id) {
        return productDAO.findById(id);
    }
    @Override
    public void insert(Product product) {
        productDAO.insert(product);
    }
    @Override
    public void update(Product product) {
        productDAO.update(product);
    }
    @Override
    public void delete(int id) {
        productDAO.delete(id);
    }
    // Lấy 10 sản phẩm mới nhất
    @Override
    public List<Product> findTop10NewProduct() {
        return productDAO.findTop10NewProduct();
    }
    // Lấy sản phẩm phân trang
    @Override
    public List<Product> findPagination(int page, int pageSize) {
        return productDAO.findPagination(page, pageSize);
    }
    // Đếm tổng sản phẩm
    @Override
    public long count() {
        return productDAO.count();
    }
}