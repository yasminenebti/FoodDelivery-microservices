package com.delivery.payment.repository;

import com.delivery.payment.entity.EPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<EPayment,Integer> {
    List<EPayment> findByPayerId(String payerId);
}
