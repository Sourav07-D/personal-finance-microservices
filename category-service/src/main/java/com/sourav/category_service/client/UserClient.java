package com.sourav.category_service.client;

import com.sourav.category_service.security.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "identity-service",
        url = "http://localhost:8081",
        configuration = FeignConfig.class
)
public interface UserClient {

    @GetMapping("/api/users/{id}")
    Object getUser(@PathVariable Long id);
}