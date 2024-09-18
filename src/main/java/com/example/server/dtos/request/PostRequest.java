package com.example.server.dtos.request;

import com.example.server.enums.PostStatusEnum;
import com.example.server.models.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequest {

    @NotBlank(message = "Title cannot be blank")
    String title;

    @Min(value = 1, message = "Star must be at least 1")
    @Max(value = 5, message = "Star cannot exceed 5")
    byte star;

    @NotBlank(message = "Address cannot be blank")
    String address;

    @NotNull(message = "Attribute ID cannot be null")
    @JsonProperty("attribute_id")
    Long attributeId;

    @NotBlank(message = "Category code cannot be blank")
    @JsonProperty("category_code")
    String categoryCode;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description;

    @NotBlank(message = "User ID cannot be blank")
    @JsonProperty("user_id")
    String userId;

    @NotNull(message = "Price number cannot be null")
    @Min(value = 0, message = "Price number cannot be negative")
    @JsonProperty("price_number")
    Float priceNumber;

    @NotBlank(message = "Price code cannot be blank")
    @JsonProperty("price_code")
    String priceCode;

    @NotNull(message = "Area number cannot be null")
    @Min(value = 0, message = "Area number cannot be negative")
    @JsonProperty("area_number")
    Float areaNumber;

    @NotBlank(message = "Area code cannot be blank")
    @JsonProperty("area_code")
    String areaCode;

    @NotBlank(message = "Province value cannot be blank")
    @JsonProperty("province_code")
    String provinceCode;

    @NotNull(message = "Price cannot be null")
    @JsonProperty("price")
    String price;

    @NotNull(message = "Acreage cannot be null")
    @JsonProperty("acreage")
    String acreage;

    @NotNull
    @JsonProperty("status")
    PostStatusEnum status;

    @NotNull(message = "Created at cannot be null")
    @JsonProperty("created_at")
    LocalDateTime createdAt;

}
