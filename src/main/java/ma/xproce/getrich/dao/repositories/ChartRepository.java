package ma.xproce.getrich.dao.repositories;

import ma.xproce.getrich.dao.entities.Chart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartRepository extends JpaRepository<Chart, Long> {
}
