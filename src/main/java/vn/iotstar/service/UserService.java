package vn.iotstar.service;
import vn.iotstar.entity.User;
public interface UserService {
    User findByUsername(String username);
    User findByEmail(String email);
    User login(String username, String password);
    void register(User user);
    void update(User user);
}