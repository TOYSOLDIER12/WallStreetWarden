package ma.xproce.getrich.dao.repositories;

import ma.xproce.getrich.dao.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    public Optional<Stock> findByTickName(String tickName);
}
