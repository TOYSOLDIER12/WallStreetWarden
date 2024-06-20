package ma.xproce.getrich.web;

import ma.xproce.getrich.service.ArimaManager;
import ma.xproce.getrich.service.EnterpriseManager;
import ma.xproce.getrich.service.PredictionManager;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger logger = Logger.getLogger(ArimaController.class.getName());
    @Autowired
    private PredictionManager predictionManager;

    @GetMapping("/forecast")
    public String getForecast(@RequestParam String stock) {
        logger.info("Received request for stock: " + stock);
        //predictionManager.addPrediction(armaManager.getForecast(stock));
        return armaManager.getForecast(stock);
    }
}