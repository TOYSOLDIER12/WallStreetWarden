package ma.xproce.getrich.dao.repositories;

import ma.xproce.getrich.dao.entities.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
}
