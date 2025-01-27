package org.example.challenge.infraestrcture.client;

import lombok.extern.slf4j.Slf4j;
import org.example.challenge.infraestrcture.cache.PercentageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
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
    @Autowired
    private RetryTemplate retryTemplate;


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
        return retryTemplate.execute(context -> {
            logger.info("Attempt {} to call external service", context.getRetryCount() + 1);
            byte[] response = restTemplate.getForObject(externalServiceUrl + "/external-service", byte[].class);
            double percentage = Double.parseDouble(new String(response));
            percentageCache.storePercentage(percentage);
            logger.info("Successfully retrieved percentage: {}", percentage);
            return percentage;
        });
    }

}
