package com.delivery.app.exception;

import lombok.*;

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
