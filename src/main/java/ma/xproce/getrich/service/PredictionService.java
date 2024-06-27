package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Stock;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PredictionService implements PredictionManager{
    @Override
    @Cacheable(value = "forecasts", key = "#stock.id")
    public Prediction getForecast(String prediction, Stock stock) {
        String forecast = prediction.split("\\[")[1].split("\\]")[0];
        String forecast_Date = prediction.split("forecast_dates\": ")[1];
        forecast_Date = forecast_Date.split("\\[")[1].split("\\]")[0];


        Prediction p = new Prediction();
        p.setStock(stock);
        p.setForecast(forecast);

        return p;
    }
}
