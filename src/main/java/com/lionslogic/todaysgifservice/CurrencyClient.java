package com.lionslogic.todaysgifservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "currency-service-client", url = "${CURRENCY_SERVICE_URL}")
public interface CurrencyClient {
    @RequestMapping(method = RequestMethod.GET, value = "/currencies.json")
    Map<String, String> getCurrencies();

    @RequestMapping(method = RequestMethod.GET, value = "/historical/{date}.json")
    CurrencyRequest getHistorical(@PathVariable String date,
                                  @RequestParam("app_id") String app_id,
                                  @RequestParam("symbols") String symbols,
                                  @RequestParam("base") String baseCurr);
}
