package ma.xproce.getrich.web;

import ma.xproce.getrich.dao.entities.Stock;
import ma.xproce.getrich.service.ArimaManager;
import ma.xproce.getrich.service.EnterpriseManager;
import ma.xproce.getrich.service.PredictionManager;
import ma.xproce.getrich.service.StockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class ArimaController {
    @Autowired
    ArimaManager armaManager;
    @Autowired
    EnterpriseManager enterpriseManager;
    @Autowired
    StockManager stockManager;
    @Autowired
    CacheManager cacheManager;

    private static final Logger logger = Logger.getLogger(ArimaController.class.getName());
    @Autowired
    private PredictionManager predictionManager;

    @GetMapping("/forecast")
    public String getForecast(@RequestParam String stock) {
        logger.info("Received request for stock: " + stock);
        Stock s = stockManager.getStockByTickName(stock);

        //caffeine cache
        Cache cache = cacheManager.getCache("forecasts");
        Cache.ValueWrapper cachedPrediction = cache.get(s.getId());

        if (cachedPrediction != null) {
            logger.info("Returning cached prediction for stock: " + stock);
            return (String) cachedPrediction.get().toString();
        }

        String prediction = armaManager.getForecast(stock);
        predictionManager.getForecast(prediction, s);

        return prediction;
    }
}