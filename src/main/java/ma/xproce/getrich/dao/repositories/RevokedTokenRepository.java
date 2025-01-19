package ma.xproce.getrich.dao.repositories;

import ma.xproce.getrich.dao.entities.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    boolean existsByToken(String token);
}
