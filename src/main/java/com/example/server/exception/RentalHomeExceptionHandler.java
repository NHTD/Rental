package com.example.server.exception;

import com.example.server.utils.RentalHomeRestApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RentalHomeExceptionHandler {

    @ExceptionHandler(RentalHomeDataModelNotFoundException.class)
    ResponseEntity<RentalHomeErrorStatus> RentalHomeDataModelNotFoundException(RentalHomeDataModelNotFoundException exception){
        return buildRentalHomeExceptionHandler(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(RentalHomeDataInvalidException.class)
    ResponseEntity<RentalHomeErrorStatus> RentalHomeDataInvalidException(RentalHomeDataInvalidException exception){
        return buildRentalHomeExceptionHandler(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(RentalHomeForbiddenException.class)
    ResponseEntity<RentalHomeErrorStatus> RentalHomeForbiddenException(RentalHomeForbiddenException exception){
        return buildRentalHomeExceptionHandler(HttpStatus.FORBIDDEN, exception);
    }

    private <E extends Throwable> ResponseEntity<RentalHomeErrorStatus> buildRentalHomeExceptionHandler(
            HttpStatus httpStatus, E exception
    ) {
        RentalHomeErrorStatus rentalHomeErrorStatus;
        rentalHomeErrorStatus = RentalHomeRestApiResponse.buildRentalHomeErrorResponse(httpStatus, exception.getMessage(), null);

        return new ResponseEntity<>(rentalHomeErrorStatus, httpStatus);
    }

}
