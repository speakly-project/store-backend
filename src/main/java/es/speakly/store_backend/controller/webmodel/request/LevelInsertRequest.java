package es.speakly.store_backend.controller.webmodel.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LevelInsertRequest(
    String name
) {}

