package com.example.server.dtos.response;

import com.example.server.enums.PostStatusEnum;
import com.example.server.models.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JsoupResponse {
    String id;

    String title;
    int star;

    String address;

    Attribute attribute;
    Category category;

    String description;

    User user;

    Float priceNumber;

    Price price;

    Float areaNumber;

    Area area;

    Province province;

    List<ImageResponse> images;

    @NotNull
    @JsonProperty("status")
    PostStatusEnum status;

    @JsonProperty("created_at")
    LocalDateTime createdAt;

    @JsonProperty("updated_at")
    LocalDateTime updatedAt;
}
