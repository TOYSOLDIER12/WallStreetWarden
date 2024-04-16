package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.User;
import ma.xproce.getrich.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
@Service
public class UserService implements UserManager{
    @Autowired
    UserRepository userRepository;
    @Override
    public User addUser(User user) {
        if(user.getUserame().isEmpty() || user.getPassword().isEmpty()) {
            System.out.println("no username no pass? thou shall not pass");
            return null;
        }
        return  userRepository.save(user);
    }

    @Override
    public boolean deleteUser(User user) {
        Optional<User> expectedUser = userRepository.findById(user.getId());
        if (expectedUser.isEmpty()){
            System.out.println("no user with that id mate " + user.getId());
        return false;
            }
        userRepository.delete(expectedUser.get());
        return !userRepository.existsById(user.getId());
    }

    @Override
    public User updateUser(User user) {
        Optional<User> expectedUser = userRepository.findById(user.getId());
        if (expectedUser.isEmpty()){
            System.out.println("no user with that id mate " + user.getId());
            return null;
        }
        User UpdatedUser = expectedUser.get();
        UpdatedUser.setName(user.getName());
        UpdatedUser.setUserame(user.getUserame());
        UpdatedUser.setPassword(user.getPassword());
        UpdatedUser.setProfile(user.getProfile());
        UpdatedUser.setNumb_Of_Interactions(user.getNumb_Of_Interactions());
        return userRepository.save(UpdatedUser);
    }

    @Override
    public User getById(long id) {
        if(userRepository.getById(id) == null) {
            System.out.println("no user with that id "+id);
            return null;
        }
        return userRepository.getById(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkLogin(String username, String password) {
        Optional<User> existingUser = userRepository.findByUserame(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getPassword().equals(password)) {
                // Password matches, login successful
                return true;
            } else {
                System.out.println("Password not matchy matchy");
                return false;
            }
        } else {
            System.out.println("who are you mr " +username+" ?");
            return false;
        }
    }


    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> existingUser = userRepository.findByUserame(username);
        if(existingUser.isPresent()){
            return existingUser;
        }
        return null;
    }

}
