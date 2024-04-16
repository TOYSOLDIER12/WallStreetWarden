package ma.xproce.getrich.dao.repositories;

import ma.xproce.getrich.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUserame(String username);
}
