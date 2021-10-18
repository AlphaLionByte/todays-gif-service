package com.lionslogic.todaysgifservice;

import java.util.Map;
import java.util.HashMap;

public class CurrencyRequest {
    private Map<String, Double> rates;

    public Map<String, Double> getRates() {
        return rates;
    }

    public CurrencyRequest() {
        this.rates = new HashMap<>();
    }
}
