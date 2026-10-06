package vn.iotstar.service;

import vn.iotstar.dao.CategoryDAO;
import vn.iotstar.dao.CategoryDAOImpl;
import vn.iotstar.entity.Category;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Category findById(int id) {
        return categoryDAO.findById(id);
    }

    @Override
    public void insert(Category category) {
        categoryDAO.insert(category);
    }

    @Override
    public void update(Category category) {
        categoryDAO.update(category);
    }

    @Override
    public void delete(int id) {
        categoryDAO.delete(id);
    }

    @Override
    public boolean hasProducts(int id) {
        return categoryDAO.hasProducts(id);
    }
}
