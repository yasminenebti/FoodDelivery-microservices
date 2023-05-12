package com.delivery.payment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    @NotNull
    private Double total;
    @NotEmpty
    private String currency;
    @NotEmpty
    private String method;
    @NotEmpty
    private String intent;
    @NotEmpty
    private String description;

    private String cancelUrl = "http://localhost:9090/pay/success";
    private String successUrl = "http://localhost:9090/pay/cancel";
}
