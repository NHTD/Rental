package com.example.server.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistrictRequest {

    @JsonProperty("district_name")
    String districtName;

    @JsonProperty("district_type")
    String districtType;

    @JsonProperty("province_code")
    String provinceCode;
}
