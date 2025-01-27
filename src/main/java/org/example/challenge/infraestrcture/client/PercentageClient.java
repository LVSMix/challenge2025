package org.example.challenge.infraestrcture.client;

import lombok.extern.slf4j.Slf4j;
import org.example.challenge.infraestrcture.cache.PercentageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class PercentageClient {

    @Autowired
    private PercentageCache percentageCache;
    private static final Logger logger = LoggerFactory.getLogger(PercentageClient.class);
    private static final int MAX_RETRIES = 3;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${external.service.url}")
    private String externalServiceUrl;


    // Simula un servicio externo que retorna un porcentaje fijo (10%)
    public double getPercentage() {
        try {
            return retryExternalService();
        } catch  (HttpClientErrorException e){
            logger.error("HTTP error occurred: {}", e.getMessage());
            return percentageCache.getPercentage()
                    .orElseThrow(() -> new RuntimeException(e.getMessage(), e));

        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage());
            return percentageCache.getPercentage()
                    .orElseThrow(() -> new RuntimeException("El servicio externo falló y no hay un valor en caché.", e));
        }
    }


    private double retryExternalService() throws Exception {
        int attempts = 0;

        while (attempts < MAX_RETRIES) {
            try {
                attempts++;
                logger.info("Attempt {} to call external service", attempts);
                // Simulación del servicio externo (aquí puedes lanzar excepciones para simular fallos)
                byte[] response = restTemplate.getForObject(externalServiceUrl + "/external-service", byte[].class);
                double percentage = Double.parseDouble(new String(response));
                percentageCache.storePercentage(percentage);
                logger.info("Successfully retrieved percentage: {}", percentage);
                return percentage;
            } catch (HttpClientErrorException.TooManyRequests e) {
                logger.warn("Attempt {} failed with 429 Too Many Requests: {}", attempts, e.getMessage());
                if (attempts == MAX_RETRIES) {
                    throw e;
                }
            } catch (Exception e) {
                logger.warn("Attempt {} failed: {}", attempts, e.getMessage());
                if (attempts == MAX_RETRIES) {
                    throw e;
                }
                // Log the exception and retry
               logger.info("Attempt " + attempts + " failed: " + e.getMessage());
            }
        }
        throw new RuntimeException("Fallo inesperado en el cliente externo."); // Este caso nunca debería alcanzarse
    }

}
