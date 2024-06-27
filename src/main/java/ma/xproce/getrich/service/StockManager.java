package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Stock;

import java.util.List;

public interface StockManager {
    public Stock addStock(Stock stock);
    public Stock updateStock(Stock stock);
    public boolean deleteStock(Stock stock);
    public Stock getStockById(long id);
    public List<Stock> findAllStock();
    public Stock getStockByTickName(String tick);

}
