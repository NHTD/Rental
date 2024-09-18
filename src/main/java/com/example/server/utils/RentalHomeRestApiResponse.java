package com.example.server.utils;

import com.example.server.exception.RentalHomeErrorDetail;
import com.example.server.exception.RentalHomeErrorStatus;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

public class RentalHomeRestApiResponse {

    public static RentalHomeErrorStatus buildRentalHomeErrorResponse(
            HttpStatus httpStatus, String message, List<RentalHomeErrorDetail> errorDetails
    ){
        RentalHomeErrorStatus rentalHomeErrorStatus = new RentalHomeErrorStatus();
        rentalHomeErrorStatus.setMessage(message);
        rentalHomeErrorStatus.setHttpStatus(httpStatus);
        rentalHomeErrorStatus.setCreatedAt(new Date());
        if(errorDetails != null){
            rentalHomeErrorStatus.setErrorDetails(errorDetails);
        }

        return rentalHomeErrorStatus;
    }

}
