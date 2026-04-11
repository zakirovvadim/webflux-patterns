package ru.vadim.webfluxpatterns.sec04.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec04.dto.InventoryRequest;
import ru.vadim.webfluxpatterns.sec04.dto.InventoryResponse;
import ru.vadim.webfluxpatterns.sec04.dto.Status;

@Service
public class InventoryClient {

    private static final String DEDUCT = "deduct";
    private static final String RESTORE = "restore";
    private final WebClient client;

    public InventoryClient(@Value("${sec04.inventory.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<InventoryResponse> deduct(InventoryRequest request) {
        return this.callUserService(DEDUCT, request);
    }

    public Mono<InventoryResponse> restore(InventoryRequest request) {
        return this.callUserService(RESTORE, request);
    }

    public Mono<InventoryResponse> callUserService(String endpoint, InventoryRequest request) {
        return this.client
                .post()
                .uri(endpoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .onErrorReturn(this.buildErrorResponse(request));
    }

    private InventoryResponse buildErrorResponse(InventoryRequest request) {
        return InventoryResponse.create(
                null,
                request.getProductId(),
                request.getQuantity(),
                null,
                Status.FAILED);
    }
}
