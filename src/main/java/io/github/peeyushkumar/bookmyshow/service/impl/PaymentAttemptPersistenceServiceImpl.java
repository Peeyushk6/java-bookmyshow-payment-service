package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import io.github.peeyushkumar.bookmyshow.exception.paymentattempt.PaymentAttemptNotFoundException;
import io.github.peeyushkumar.bookmyshow.repository.PaymentAttemptRepository;
import io.github.peeyushkumar.bookmyshow.service.PaymentAttemptPersistenceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentAttemptPersistenceServiceImpl implements PaymentAttemptPersistenceService
{

    private final PaymentAttemptRepository repository;

    @Override
    public PaymentAttempt save(
            PaymentAttempt paymentAttempt
    ){
        return  repository.save(paymentAttempt);
    }

    @Override
    public PaymentAttempt update(
            PaymentAttempt paymentAttempt
    ){
        return repository.save(paymentAttempt);
    }

    @Override
    public PaymentAttempt findById(Long attemptId){

        return repository.findById(attemptId)
                .orElseThrow(
                        () -> new PaymentAttemptNotFoundException(attemptId)
                );
    }

    @Override
    public Integer nextAttemptNumber(
            Payment payment,
            PaymentAttemptType attemptType
    )
    {

        return repository.findMaxAttemptNumber(payment,attemptType) + 1;
    }
}
