package org.example.challenge.infraestrcture.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
public class WireMockInitializer implements CommandLineRunner {

    @Autowired
    private WireMockServer wireMockServer;

    @Override
    public void run(String... args) throws Exception {
        wireMockServer.stubFor(get(urlEqualTo("/external-service"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("10.0")));
    }
}
