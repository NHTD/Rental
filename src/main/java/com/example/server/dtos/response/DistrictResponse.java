package com.example.server.dtos.response;

import com.example.server.models.Province;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistrictResponse {
    Long id;
    String districtName;

    String districtType;

    Province province;
}
