package ma.xproce.getrich.service;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class FileService implements  FileManager{

    private static final Logger logger = Logger.getLogger(arimaService.class.getName());
    @Override
    public boolean downloadCsv(String ticker) {

        try {
            // Command to activate the virtual environment and run the Python script
            String[] cmd = {"/bin/bash", "-c", "source python/venv/bin/activate && python3 python/stock_utils.py " + ticker};
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info("CSV download completed successfully for ticker: " + ticker);
                return true;
            } else {
                logger.severe("Stock Python script failed with exit code: " + exitCode);
                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Error executing Stock Python script: " + e.getMessage());
            return false;
        }

    }
}
