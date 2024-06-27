package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Stock;

public interface PredictionManager {
    public Prediction getForecast(String prediction, Stock stock);
}
