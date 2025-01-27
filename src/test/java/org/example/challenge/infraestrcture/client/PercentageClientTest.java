package org.example.challenge.infraestrcture.client;


import org.example.challenge.infraestrcture.cache.PercentageCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PercentageClientTest {


    @Mock
    private PercentageCache percentageCache;

    @InjectMocks
    private PercentageClient percentageClient;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        // Set the externalServiceUrl to the WireMock server URL
        String externalServiceUrl = "http://localhost:" + wireMockServer.port();
        ReflectionTestUtils.setField(percentageClient, "externalServiceUrl", externalServiceUrl);

        // Configurar WireMock para fallar las dos primeras veces y tener éxito en la tercera
        stubFor(get(urlEqualTo("/external-service"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse().withStatus(500).withBody("Service Unavailable"))
                .willSetStateTo("Second Attempt"));

        stubFor(get(urlEqualTo("/external-service"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("Second Attempt")
                .willReturn(aResponse().withStatus(500).withBody("Service Unavailable"))
                .willSetStateTo("Third Attempt"));

        stubFor(get(urlEqualTo("/external-service"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("Third Attempt")
                .willReturn(aResponse().withStatus(200).withBody("10.0")));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getPercentage_ShouldReturnFixedPercentage_WhenExternalServiceSucceeds() throws Exception {
        doNothing().when(percentageCache).storePercentage(anyDouble());

        double result = percentageClient.getPercentage();

        assertEquals(10.0, result);
        verify(percentageCache, times(1)).storePercentage(10.0);
    }

    @Test
    void getPercentage_ShouldReturnCachedPercentage_WhenExternalServiceFails() throws Exception {
        when(percentageCache.getPercentage()).thenReturn(Optional.of(5.0));
        wireMockServer.stop(); // Ensure the external service fails
        double result = percentageClient.getPercentage();

        assertEquals(5.0, result);
        verify(percentageCache, times(1)).getPercentage();
    }

    @Test
    void getPercentage_ShouldThrowException_WhenExternalServiceAndCacheFail() {
        when(percentageCache.getPercentage()).thenReturn(Optional.empty());
        wireMockServer.stop(); // Ensure the external service fails
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            percentageClient.getPercentage();
        });

        assertEquals("El servicio externo falló y no hay un valor en caché.", exception.getMessage());
        verify(percentageCache, times(1)).getPercentage();
    }
}
