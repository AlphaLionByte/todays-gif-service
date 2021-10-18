package com.lionslogic.todaysgifservice;

public class Gifting {
    private final double prevDayAmount;
    private final double curDayAmount;
    private final String gifUrl;

    public double getPrevDayAmount() {
        return prevDayAmount;
    }

    public double getCurDayAmount() {
        return curDayAmount;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public Gifting(double prevDayAmount, double curDayAmount, String gifUrl) {
        this.prevDayAmount = prevDayAmount;
        this.curDayAmount = curDayAmount;
        this.gifUrl = gifUrl;
    }
}
