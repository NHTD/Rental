package com.example.server.dtos.response;

import com.example.server.enums.PostStatusEnum;
import com.example.server.models.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class PostResponse {
    String id;

    String title;
    byte star;

    String address;

    Attribute attribute;
    Category category;

    String description;

    User user;

    String cloudinaryImageId;

    Float priceNumber;

    String priceCode;

    Float areaNumber;

    String areaCode;

    String provinceCode;

    List<ImageResponse> images;

    @NotNull
    @JsonProperty("status")
    PostStatusEnum status;

    @JsonProperty("created_at")
    LocalDateTime createdAt;

    @JsonProperty("updated_at")
    LocalDateTime updatedAt;
}