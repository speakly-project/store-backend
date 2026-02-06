package es.speakly.store_backend.controller.webmodel.request;

public record OrderUpdateRequest(
        Long id,
        Long userId,
        Long[] courseIds,
        String status
) {
}
