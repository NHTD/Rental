package com.example.server.dtos.request;

import com.example.server.enums.PostStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JsoupRequest {
    String title;

    @Min(value = 1, message = "Star must be at least 1")
    @Max(value = 5, message = "Star cannot exceed 5")
    byte star;

    String address;

    @JsonProperty("attribute_id")
    Long attributeId;

    @JsonProperty("category_code")
    String categoryCode;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description;

    @JsonProperty("user_id")
    String userId;

    @JsonProperty("price_number")
    String priceNumber;

    @JsonProperty("price_code")
    String priceCode;

    @JsonProperty("area_number")
    String areaNumber;

    @JsonProperty("area_code")
    String areaCode;

    @JsonProperty("province_code")
    String provinceCode;

    @JsonProperty("acreage")
    String acreage;

    @NotNull
    @JsonProperty("status")
    PostStatusEnum status;

    @JsonProperty("created_at")
    LocalDateTime createdAt;
}
