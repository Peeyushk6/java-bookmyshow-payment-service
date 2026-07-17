package io.github.peeyushkumar.bookmyshow.repository;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRepository
        extends JpaRepository<Refund, Long> {

    List<Refund> findByPayment(Payment payment);

}