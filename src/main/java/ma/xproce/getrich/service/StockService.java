package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Stock;
import ma.xproce.getrich.dao.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StockService implements StockManager{
    @Autowired
    StockRepository stockRepository;
    @Override
    public Stock addStock(Stock stock) {
        if(stock.getEnterprise() == null || stock.getPrice() == 0)
            return null;
        return stockRepository.save(stock);
    }

    @Override
    public Stock updateStock(Stock stock) {
        Optional<Stock> stockOptional = stockRepository.findById(stock.getId());
        if(stockOptional.isEmpty())
            return null;
        Stock stockToUpdate = stockOptional.get();
        stockToUpdate.setEnterprise(stock.getEnterprise());
        stockToUpdate.setPrice(stock.getPrice());
        stockToUpdate.setTickName(stock.getTickName());
        return stockRepository.save(stockToUpdate);
    }

    @Override
    public boolean deleteStock(Stock stock) {
        Optional<Stock> stockOptional = stockRepository.findById(stock.getId());
        if(stockOptional.isEmpty()) {
            System.out.println("no stock found");
            return false;
        }
        stockRepository.delete(stockOptional.get());
        return !stockRepository.existsById(stock.getId());
    }

    @Override
    public Optional<Stock> getStockById(long id) {
        return stockRepository.findById(id);
    }

    @Override
    public List<Stock> findAllStock() {
        return stockRepository.findAll();
    }
}
