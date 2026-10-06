package vn.iotstar.dao;

import vn.iotstar.entity.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> findAll();
    Product findById(int id);
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    List<Product> findTop10NewProduct();
    List<Product> findPagination(int page, int pageSize);
    // tính số trang
    long count();
}