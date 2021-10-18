package com.lionslogic.todaysgifservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "gif-service-client", url = "${GIF_SERVICE_URL}")
public interface GifClient {
    @RequestMapping(method = RequestMethod.GET, value = "/random")
    GifRequest getRandom(@RequestParam("api_key") String api_key,
                         @RequestParam("tag") String tagString);
}
