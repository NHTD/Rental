package com.example.server.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentalHomeErrorStatus extends AbstractRentalHomeErrorStatus{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<RentalHomeErrorDetail> errorDetails;
}
