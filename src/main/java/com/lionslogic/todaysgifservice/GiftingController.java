package com.lionslogic.todaysgifservice;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

@RestController
public class GiftingController {
    private final String currencyApiKey;
    private final String gifApiKey;

    private final String goodGifKeyword;
    private final String badGifKeyword;

    private final String defaultCurrencyBase;

    @Autowired
    private CurrencyClient currencyClient;

    @Autowired
    private GifClient gifClient;

    public GiftingController() {
        currencyApiKey = AppProperties.getProperty("CURRENCY_SERVICE_API_KEY");
        gifApiKey = AppProperties.getProperty("GIF_SERVICE_API_KEY");

        goodGifKeyword = AppProperties.getProperty("GIFKEYWORD_GOOD");
        badGifKeyword = AppProperties.getProperty("GIFKEYWORD_BAD");

        defaultCurrencyBase = AppProperties.getProperty("CURRENCY_BASE_DEFAULT");
    }

    @GetMapping("/getcurrencies")
    public ResponseEntity<?> getCurrencies() {
        return new ResponseEntity<>(currencyClient.getCurrencies(), HttpStatus.OK);
    }

    @GetMapping("/getbasecurrency")
    public ResponseEntity<?> getBaseCurrency() {
        return new ResponseEntity<>(defaultCurrencyBase, HttpStatus.OK);
    }

    @GetMapping("/gettodaysgif")
    public ResponseEntity<?> getTodaysGif(@RequestParam(value = "ccode", defaultValue = "RUB") String ccode,
                                          @RequestParam(value = "bccode", defaultValue = "") String bccode) {
        ccode = ccode.trim().toUpperCase(Locale.ROOT);
        bccode = bccode.trim().toUpperCase(Locale.ROOT);
        if (bccode.isEmpty())
            bccode = defaultCurrencyBase;

        try {
            Map<String, String> avalibleCurrencies = currencyClient.getCurrencies();
            if (avalibleCurrencies.containsKey(ccode) && avalibleCurrencies.containsKey(bccode)) {
                Instant curDate = Instant.now();
                Instant prevDayDate = curDate.minus(1, ChronoUnit.DAYS);

                DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));

                CurrencyRequest prevDayCurrency = currencyClient.getHistorical(dtFormatter.format(prevDayDate),
                        currencyApiKey, ccode, bccode);
                CurrencyRequest curDayCurrency = currencyClient.getHistorical(dtFormatter.format(curDate),
                        currencyApiKey, ccode, bccode);

                GifRequest gr = null;

                if (curDayCurrency.getRates().get(ccode) >= prevDayCurrency.getRates().get(ccode)) {
                    gr = gifClient.getRandom(gifApiKey, goodGifKeyword);
                } else if (curDayCurrency.getRates().get(ccode) < prevDayCurrency.getRates().get(ccode)) {
                    gr = gifClient.getRandom(gifApiKey, badGifKeyword);
                }

                if (gr != null) {
                    return new ResponseEntity<>(new Gifting(prevDayCurrency.getRates().get(ccode),
                            curDayCurrency.getRates().get(ccode),
                            gr.getData().getImage_original_url()), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
