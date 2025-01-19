package ma.xproce.getrich;

import ma.xproce.getrich.dao.entities.Stock;
import ma.xproce.getrich.service.*;
import ma.xproce.getrich.web.ArimaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArimaControllerTest {

    @InjectMocks
    private ArimaController arimaController;

    @Mock
    private ArimaManager arimaManager;

    @Mock
    private EnterpriseManager enterpriseManager;

    @Mock
    private StockManager stockManager;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private PredictionManager predictionManager;

    @Mock
    private Cache cache;

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setId(1L);
        stock.setTickName("AAPL");

        when(stockManager.getStockByTickName("AAPL")).thenReturn(stock);
        when(cacheManager.getCache("forecasts")).thenReturn(cache);
    }

    @Test
    void testGetForecast_CacheHit() {
        // Arrange
        Map<String, Object> cachedResponse = new HashMap<>();
        cachedResponse.put("values", List.of(100.0, 101.0, 102.0));
        cachedResponse.put("dates", List.of("2024-09-27", "2024-09-28", "2024-09-29"));

        when(cache.get("1_3")).thenReturn(() -> cachedResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = arimaController.getForecast("AAPL", "3");

        // Assert
        assertEquals(ResponseEntity.ok(cachedResponse), response);
        verify(cache).get("1_3");
        verifyNoMoreInteractions(arimaManager, predictionManager);
    }

    @Test
    void testGetForecast_CacheMiss() {
        // Arrange
        when(cache.get("1_3")).thenReturn(null);
        when(arimaManager.getForecast("AAPL", "3")).thenReturn("100.0, 101.0, 102.0");

        // Assuming your predictionManager's getForecast method is correctly implemented.
        when(predictionManager.getForecast("100.0, 101.0, 102.0", stock)).thenReturn(new Prediction(/*...*/));

        // Act
        ResponseEntity<Map<String, Object>> response = arimaController.getForecast("AAPL", "3");

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("values"));
        assertTrue(response.getBody().containsKey("dates"));
        verify(cache).put("1_3", response.getBody());
    }

    @Test
    void testGetForecast_InvalidStock() {
        // Arrange
        when(stockManager.getStockByTickName("INVALID")).thenReturn(null);

        // Act
        ResponseEntity<Map<String, Object>> response = arimaController.getForecast("INVALID", "3");

        // Assert
        assertEquals(404, response.getStatusCodeValue());
    }
}
