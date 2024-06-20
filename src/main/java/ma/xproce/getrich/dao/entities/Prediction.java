package ma.xproce.getrich.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Prediction {
    private long id;
    private Date firstStockDate;
    private Map<String,String> predictions;
    private Enterprise enterprise;
}
