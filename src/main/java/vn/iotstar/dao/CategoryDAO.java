package vn.iotstar.dao;

import vn.iotstar.entity.Category;
import java.util.List;

public interface CategoryDAO {
    List<Category> findAll();
    Category findById(int id);
    void insert(Category category);
    void update(Category category);
    void delete(int id);
    boolean hasProducts(int id);
}
