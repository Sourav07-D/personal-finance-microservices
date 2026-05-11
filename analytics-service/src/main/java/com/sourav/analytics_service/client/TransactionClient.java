package com.sourav.analytics_service.client;

import com.sourav.analytics_service.dto.TransactionResponseDTO;
import com.sourav.analytics_service.security.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "transaction-service",
        url = "http://localhost:8082"
     //   configuration = FeignConfig.class
)
public interface TransactionClient {

    @GetMapping("/api/transactions/user/{userId}/all")
    List<TransactionResponseDTO> getAllTransactions(
            @PathVariable Long userId
    );
}