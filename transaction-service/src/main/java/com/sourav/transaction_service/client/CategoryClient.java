package com.sourav.transaction_service.client;

import com.sourav.transaction_service.security.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "category-service",
        url = "http://localhost:8083",
        configuration = FeignConfig.class
)
public interface CategoryClient {

    @GetMapping("/api/categories/{id}")
    Object getCategory(@PathVariable Long id);
}