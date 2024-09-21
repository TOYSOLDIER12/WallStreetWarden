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
import java.util.List;
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
    private java.util.stream.Collectors Collectors;

    @GetMapping("/forecast")
    public ResponseEntity<Map<String, Object>> getForecast(@RequestParam String stock, @RequestParam String steps) {
        logger.info("Received request for stock: " + stock +"with "+ steps +" steps");
        Stock s = stockManager.getStockByTickName(stock);

        String cacheKey = s.getId() + "_" + steps;

        //caffeine cache
        Cache cache = cacheManager.getCache("forecasts");
        Cache.ValueWrapper cachedPrediction = cache.get(cacheKey);

        if (cachedPrediction != null) {
            logger.info("Returning cached prediction for stock: " + stock +"with "+ steps +" steps");
            return ResponseEntity.ok((Map<String, Object>) cachedPrediction.get());
        }

            String forecast = armaManager.getForecast(stock, steps);
            Prediction prediction =  predictionManager.getForecast(forecast, s);

            Map<String, Object> response = new HashMap<>();

            List<Double> forecastValues = Arrays.stream(prediction.getForecast().split(", "))
                .map(Double::parseDouble) // Convert to Double
                .collect(Collectors.toList());

            List<String> forecastDates = Arrays.asList(prediction.getForecastDate().replaceAll("\"", "").split(", "));

            response.put("values", forecastValues);
            response.put("dates", forecastDates);

            cache.put(cacheKey, response);

            return ResponseEntity.ok(response);
    }
}