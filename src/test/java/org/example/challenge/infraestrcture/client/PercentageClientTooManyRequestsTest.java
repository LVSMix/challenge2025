package org.example.challenge.infraestrcture.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.example.challenge.infraestrcture.cache.PercentageCache;
import org.example.challenge.infraestrcture.config.RetryConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

public class PercentageClientTooManyRequestsTest {

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

        // Create and inject RetryTemplate
        RetryConfig retryConfig = new RetryConfig();
        RetryTemplate retryTemplate = retryConfig.retryTemplate();
        ReflectionTestUtils.setField(percentageClient, "retryTemplate", retryTemplate);


        // Configure WireMock to return a 200 status code on the first call and a 429 status code on the second call
        stubFor(get(urlEqualTo("/external-service"))
                .inScenario("Too Many Requests Scenario")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse().withStatus(200).withBody("10.0"))
                .willSetStateTo("Second Call"));

        stubFor(get(urlEqualTo("/external-service"))
                .inScenario("Too Many Requests Scenario")
                .whenScenarioStateIs("Second Call")
                .willReturn(aResponse().withStatus(429).withBody("Too Many Requests")));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getPercentage_ShouldThrowException_WhenRpmLimitExceeded() {
        when(percentageCache.getPercentage()).thenReturn(Optional.empty());

        // First call should succeed
        double percentage = percentageClient.getPercentage();
        assertEquals(10.0, percentage);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            percentageClient.getPercentage();
        });

        assertEquals("429 Too Many Requests: \"Too Many Requests\"", exception.getMessage());
        verify(percentageCache, times(1)).getPercentage();
    }
}
