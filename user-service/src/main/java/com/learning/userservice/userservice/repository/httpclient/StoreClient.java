package com.learning.userservice.userservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "store-service", url = "${store-service.url}")
public interface StoreClient {
    @GetMapping("/own-store")
    Object getOwnStores();
}
