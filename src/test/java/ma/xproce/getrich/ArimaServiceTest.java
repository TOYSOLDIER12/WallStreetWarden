package ma.xproce.getrich;

import ma.xproce.getrich.service.arimaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ArimaServiceTest {

    @Test
    public void testGetForecastSuccess() throws Exception {
        arimaService arimaService = new arimaService();

        // Mock Process and ProcessBuilder
        Process mockProcess = mock(Process.class);
        ProcessBuilder mockProcessBuilder = mock(ProcessBuilder.class);

        // Mock the input stream that comes from the process
        InputStream inputStream = new ByteArrayInputStream("Forecast result".getBytes());
        when(mockProcess.getInputStream()).thenReturn(inputStream);

        // Mock the behavior of ProcessBuilder starting the process
        when(mockProcessBuilder.start()).thenReturn(mockProcess);

        // Spy on the arimaService and mock the getProcessBuilder behavior
        arimaService spyArimaService = Mockito.spy(arimaService);
        doReturn(mockProcessBuilder).when(spyArimaService).getProcessBuilder(any(), any());

        // Act
        String result = spyArimaService.getForecast("AAPL", "10");

        String expectedResult = "{\"forecast\": [227.37368563295658, 227.54063417011608, ...], \"forecast_dates\": [\"2024-09-27\", ...]}";

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetForecastFailure() throws Exception {
        // Arrange
        arimaService arimaService = new arimaService();

        // Spy on the service and mock an exception during process creation
        arimaService spyArimaService = Mockito.spy(arimaService);
        doThrow(new RuntimeException("Mocked exception")).when(spyArimaService).getProcessBuilder(any(), any());

        // Act
        String result = spyArimaService.getForecast("AAPL", "10");

        // Assert
        assertTrue(result.contains("Error: Mocked exception"));
    }
}

