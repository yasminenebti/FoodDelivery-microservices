package com.delivery.payment.Controller;

import com.delivery.payment.dto.PaymentDTO;
import com.delivery.payment.service.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public String payment(@RequestBody PaymentDTO paymentDTO,
                          @RequestHeader("x-auth-user-id") Integer userId) throws PayPalRESTException {

        Payment payment = paymentService.createPayment(paymentDTO , userId);
        for (Links link:payment.getLinks()) {
            if (link.getRel().equals("approval_url")){
                return "redirect to : "+link.getHref();
            }
        }
        return "redirect:/";
    }

    @GetMapping("/pay/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestHeader("x-auth-user-id") String payerId) throws PayPalRESTException {
        Payment payment = paymentService.executePayment(paymentId, payerId);
        System.out.println(payment.toJSON());
        return ResponseEntity.ok("payment_success");
    }

    @GetMapping("/pay/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("payment_cancel");
    }

    @GetMapping("/payments")
    public ResponseEntity<?> getAllPayments(Integer userId){
        return ResponseEntity.ok(paymentService.getAllPaymentsPerUser(userId));
    }

}
