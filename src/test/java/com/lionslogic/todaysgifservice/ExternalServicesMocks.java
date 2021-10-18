package com.lionslogic.todaysgifservice;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import static java.nio.charset.Charset.defaultCharset;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static org.springframework.util.StreamUtils.copyToString;

public class ExternalServicesMocks {
    public static void setupGifServiceMockResponses(WireMockServer mockServer) throws IOException {
        String gifApiKey = AppProperties.getProperty("GIF_SERVICE_API_KEY");
        // get for random
        mockServer.stubFor(WireMock.get(WireMock.urlMatching("/v1/gifs/random\\?api_key=" + gifApiKey + "&tag=([a-z]*)"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        ExternalServicesMocks.class.getClassLoader().getResourceAsStream("payload/gif-service_get-for_random.json"),
                                        defaultCharset()))));
    }
    public static void setupCurrencyServiceMockResponses(WireMockServer mockServer) throws IOException {
        String currencyApiKey = AppProperties.getProperty("CURRENCY_SERVICE_API_KEY");
        String dateRegex = "\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])*";

        // get for currencies
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/api/currencies.json"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        ExternalServicesMocks.class.getClassLoader().getResourceAsStream("payload/currency-service_get-for_currencies.json"),
                                        defaultCharset()))));

        // get for historical
        mockServer.stubFor(WireMock.get(WireMock.urlMatching("/api/historical/" + dateRegex + ".json\\?app_id=" + currencyApiKey + "&symbols=([a-zA-Z]*)&base=([a-zA-Z]*)"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        ExternalServicesMocks.class.getClassLoader().getResourceAsStream("payload/currency-service_get-for_historical.json"),
                                        defaultCharset()))));
    }
}
