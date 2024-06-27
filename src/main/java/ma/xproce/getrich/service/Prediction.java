package ma.xproce.getrich.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.xproce.getrich.dao.entities.Stock;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prediction {
    private Date firstStockDate;
    private String Forecast;
    private String forecastDate;
    private Stock stock;

    @Override
    public String toString() {
        return this.getForecast()+getForecastDate();
    }
}
