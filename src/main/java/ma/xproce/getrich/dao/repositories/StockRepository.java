package ma.xproce.getrich.dao.repositories;

import ma.xproce.getrich.dao.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
