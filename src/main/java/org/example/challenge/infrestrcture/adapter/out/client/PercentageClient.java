package org.example.challenge.infrestrcture.adapter.out.client;

import org.example.challenge.infrestrcture.adapter.out.cache.PercentageCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PercentageClient {

    @Autowired
    private PercentageCache percentageCache;

    private static final int MAX_RETRIES = 3;

    // Simula un servicio externo que retorna un porcentaje fijo (10%)
    public double getPercentage() {
        int attempts = 0;

        while (attempts < MAX_RETRIES) {
            try {
                attempts++;
                // Simulación del servicio externo (aquí puedes lanzar excepciones para simular fallos)
                double percentage = simulateExternalService();
                percentageCache.storePercentage(percentage);
                return percentage;
            } catch (Exception e) {
                if (attempts == MAX_RETRIES) {
                    // Al alcanzar el límite de reintentos, usar la caché si está disponible
                    return percentageCache.getPercentage()
                            .orElseThrow(() -> new RuntimeException("El servicio externo falló y no hay un valor en caché."));
                }
            }
        }
        throw new RuntimeException("Fallo inesperado en el cliente externo."); // Este caso nunca debería alcanzarse

    }

    // Método que simula el servicio externo
    private double simulateExternalService() throws Exception {
        // Simula una falla aleatoria para probar la lógica de reintento
        if (Math.random() > 0.7) {
            throw new Exception("Simulación de fallo del servicio externo");
        }
        return 10.0; // Retorna un porcentaje fijo del 10% en caso de éxito
    }
}
