package com.delivery.payment.service;

import com.delivery.payment.dto.PaymentDTO;
import com.delivery.payment.entity.ECurrency;
import com.delivery.payment.entity.EPayment;
import com.delivery.payment.entity.Method;
import com.delivery.payment.exception.ResourceNotFoundException;
import com.delivery.payment.repository.PaymentRepository;
import com.delivery.payment.validation.ObjectValidator;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    private final ObjectValidator<PaymentDTO> objectValidator;
    private final PaymentRepository paymentRepository;

    private final APIContext apiContext;

    public PaymentService(ObjectValidator<PaymentDTO> objectValidator, PaymentRepository paymentRepository, APIContext apiContext) {
        this.objectValidator = objectValidator;
        this.paymentRepository = paymentRepository;
        this.apiContext = apiContext;
    }


    public Payment createPayment(PaymentDTO request , Integer userId) throws PayPalRESTException {
        objectValidator.validate(request);
        Amount amount = new Amount();
        amount.setCurrency(request.getCurrency());
        amount.setTotal(request.getTotal().toString());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(request.getDescription());
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(request.getMethod());

        Payment payment = new Payment();
        payment.setIntent(request.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(request.getCancelUrl());
        redirectUrls.setReturnUrl(request.getSuccessUrl());
        payment.setRedirectUrls(redirectUrls);

        EPayment ePayment = EPayment.builder()
                .description(transaction.getDescription())
                .payerId(userId.toString())
                .total(request.getTotal())
                .intent(payment.getIntent())
                .currency(ECurrency.valueOf(request.getCurrency()))
                .method(Method.valueOf(request.getMethod()))
                .build();
        paymentRepository.save(ePayment);
        return payment.create(apiContext);

    }
    public Payment executePayment(String paymentId , String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext,paymentExecution);

    }

    public List<EPayment> getAllPaymentsPerUser(Integer userId) {
        List<EPayment> payments = paymentRepository.findByPayerId(userId.toString());
        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("This user did not make any payments");
        }
        return payments;
    }


}
