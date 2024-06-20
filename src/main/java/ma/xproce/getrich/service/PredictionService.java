package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Prediction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PredictionService implements PredictionManager{
    @Override
    public boolean addPrediction(String prediction) {
        String forecast = prediction.split("\\[")[1].split("\\]")[0];
        String forecast_Date = prediction.split("forecast_dates\": ")[1];
        forecast_Date = forecast_Date.split("\\[")[1].split("\\]")[0];
        Prediction p = new Prediction();
        Map<String , String> map = new HashMap<>();
        map.put("forecast_date", forecast_Date);
        p.setPredictions(map);

        return false;
    }
}
