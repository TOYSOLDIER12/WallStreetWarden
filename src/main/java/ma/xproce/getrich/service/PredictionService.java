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


        // Extract and clean the forecast dates
        String forecastDate = prediction.split("forecast_dates\": ")[1];
        forecastDate = forecastDate.split("\\[")[1].split("\\]")[0];


        Prediction p = new Prediction();
        p.setStock(stock);
        p.setForecast(forecast);
        p.setForecastDate(forecastDate);

        return p;
    }
}
