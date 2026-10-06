package vn.iotstar.dao;
import vn.iotstar.entity.User;
public interface UserDAO {
    User findByUsername(String username);
    User findByEmail(String email);
    User login(String username, String password);
    void insert(User user);
    void update(User user);
}