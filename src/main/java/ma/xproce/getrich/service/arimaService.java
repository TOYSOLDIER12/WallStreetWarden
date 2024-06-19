package ma.xproce.getrich.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

@Service
public class arimaService implements ArimaManager {

    private static final Logger logger = Logger.getLogger(arimaService.class.getName());

    public String getForecast( String stock){

        try {
            // Command to activate the virtual environment and run the Python script
            String[] cmd = {"/bin/bash", "-c", "source python/venv/bin/activate && python3 python/arima.py " + stock};
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            logger.info("Python script output: " + result.toString());
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Error executing Python script: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
