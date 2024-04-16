package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.User;

import java.util.Collection;
import java.util.Optional;

public interface UserManager {
    public User addUser(User user);
    public boolean deleteUser(User user);
    public User updateUser(User user);
    public User getById(long id);
    public Collection<User> getAllUsers();
    public boolean checkLogin(String username, String password);
    public Optional<User> findByUsername(String username);

}
