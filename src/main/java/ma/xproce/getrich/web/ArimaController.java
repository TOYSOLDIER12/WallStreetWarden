package ma.xproce.getrich.web;

import ma.xproce.getrich.dao.entities.Stock;
import ma.xproce.getrich.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Map<String, Object>> getForecast(@RequestParam String stock) {
        logger.info("Received request for stock: " + stock);
        Stock s = stockManager.getStockByTickName(stock);

        //caffeine cache
        Cache cache = cacheManager.getCache("forecasts");
        Cache.ValueWrapper cachedPrediction = cache.get(s.getId());

        if (cachedPrediction != null) {
            logger.info("Returning cached prediction for stock: " + stock);
            return ResponseEntity.ok((Map<String, Object>) cachedPrediction.get());
        }

        String forecast = armaManager.getForecast(stock);
        Prediction prediction =  predictionManager.getForecast(forecast, s);

        Map<String, Object> response = new HashMap<>();
        response.put("values", Arrays.asList(prediction.getForecast().split(", ")));
        response.put("dates", Arrays.asList(prediction.getForecastDate().split(", ")));

        cache.put(s.getId(), response);

        return ResponseEntity.ok(response);
    }
}