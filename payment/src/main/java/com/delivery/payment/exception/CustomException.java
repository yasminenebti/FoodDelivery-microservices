package com.delivery.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomException {
    private String message ;
    private Date timeStamp;

    private Integer Status;

    private String details;

}
