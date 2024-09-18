package com.example.server.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractRentalHomeErrorStatus {
    @JsonIgnore
    HttpStatus httpStatus;

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String message;

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Date createdAt;

    @JsonProperty("status")
    public int getStatus(){
        return httpStatus.value();
    }

    @JsonProperty("error")
    public String getError(){
        return httpStatus.getReasonPhrase();
    }
}
