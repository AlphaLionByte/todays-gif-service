package com.lionslogic.todaysgifservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
class TodaysGifServiceApplicationTests {
	@Autowired
	public WireMockServer testMockServer;

	@Autowired
	private GiftingController giftingController;

	@Autowired
	private CurrencyClient currencyClient;

	@Autowired
	private GifClient gifClient;

	@Value("${CURRENCY_SERVICE_API_KEY}")
	private String currencyApiKey;
	@Value("${CURRENCY_BASE_DEFAULT}")
	private String currencyBase;
	@Value("${GIF_SERVICE_API_KEY}")
	private String gifApiKey;
	@Value("${GIFKEYWORD_GOOD}")
	private String gifKeyword;

	@BeforeEach
	void setUp() throws IOException {
		ExternalServicesMocks.setupGifServiceMockResponses(testMockServer);
		ExternalServicesMocks.setupCurrencyServiceMockResponses(testMockServer);
	}

	@Test
	void contextLoads() throws MalformedURLException {
		int testingPort = Integer.parseInt(AppProperties.getProperty("testing.server.port"));
		assertThat(giftingController).isNotNull();
		assertEquals(new URL(AppProperties.getProperty("CURRENCY_SERVICE_URL")).getPort(), testingPort);
		assertEquals(new URL(AppProperties.getProperty("GIF_SERVICE_URL")).getPort(), testingPort);
	}

	@Test
	void currencyService_CanGetCurrenciesTest() {
		Map<String, String> result = currencyClient.getCurrencies();
		assertFalse(result.isEmpty());
	}

	@Test
	void currencyService_CanGetHistoricalTest() {
		CurrencyRequest result = currencyClient.getHistorical("2021-10-01", currencyApiKey, "RUB", currencyBase);
		assertFalse(result.getRates().isEmpty());
	}

	@Test
	void gifService_CanGetRandomImageTest() {
		GifRequest result = gifClient.getRandom(gifApiKey, gifKeyword);
		assertNotNull(result.getData().getImage_original_url());
	}
}
