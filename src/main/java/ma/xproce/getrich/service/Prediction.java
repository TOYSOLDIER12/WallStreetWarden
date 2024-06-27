package ma.xproce.getrich.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prediction {
    private long id;
    private Date firstStockDate;
    private String Forecast;
    private String forecastDate;
    private Enterprise enterprise;
}
