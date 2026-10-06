package vn.iotstar.service;

import vn.iotstar.dao.UserDAO;
import vn.iotstar.dao.UserDAOImpl;
import vn.iotstar.entity.User;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO =
            new UserDAOImpl();
    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }
    @Override
    public User login(String username, String password) {
        return userDAO.login(username, password);
    }
    @Override
    public void register(User user) {
        userDAO.insert(user);
    }
    @Override
    public void update(User user) {
        userDAO.update(user);
    }
}