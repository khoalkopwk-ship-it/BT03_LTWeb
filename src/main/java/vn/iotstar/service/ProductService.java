package vn.iotstar.service;

import vn.iotstar.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(int id);
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    // Lấy sản phẩm mới nhất
    List<Product> findTop10NewProduct();
    // Lấy sản phẩm theo trang
    List<Product> findPagination(int page, int pageSize);

    // Đếm sản phẩm
    long count();
}