package com.lionslogic.todaysgifservice;

public class GifRequest {
    private final GifObject data;

    public GifObject getData() {
        return data;
    }

    public GifRequest(GifObject data, String keyword) {
        this.data = data;
    }
}
