package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Stock;

import java.util.List;
import java.util.Optional;

public interface StockManager {
    public Stock addStock(Stock stock);
    public Stock updateStock(Stock stock);
    public boolean deleteStock(Stock stock);
    public Optional<Stock> getStockById(long id);
    public List<Stock> findAllStock();

}
