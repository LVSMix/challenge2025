package org.example.challenge.infraestrcture.client;

import org.example.challenge.infraestrcture.cache.PercentageCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PercentageClient {

    @Autowired
    private PercentageCache percentageCache;

    private static final int MAX_RETRIES = 3;
    private static final double FIXED_PERCENTAGE = 10.0;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${external.service.url}")
    private String externalServiceUrl;


    // Simula un servicio externo que retorna un porcentaje fijo (10%)
    public double getPercentage() {
        try {
            return retryExternalService();
        } catch (Exception e) {
            return percentageCache.getPercentage()
                    .orElseThrow(() -> new RuntimeException("El servicio externo falló y no hay un valor en caché.", e));
        }
    }


    private double retryExternalService() throws Exception {
        int attempts = 0;

        while (attempts < MAX_RETRIES) {
            try {
                attempts++;
                // Simulación del servicio externo (aquí puedes lanzar excepciones para simular fallos)
                byte[] response = restTemplate.getForObject(externalServiceUrl + "/external-service", byte[].class);
                double percentage = Double.parseDouble(new String(response));
                percentageCache.storePercentage(percentage);
                return percentage;
            } catch (Exception e) {
                if (attempts == MAX_RETRIES) {
                    throw e;
                }
                // Log the exception and retry
                System.err.println("Attempt " + attempts + " failed: " + e.getMessage());
            }
        }
        throw new RuntimeException("Fallo inesperado en el cliente externo."); // Este caso nunca debería alcanzarse
    }

}
