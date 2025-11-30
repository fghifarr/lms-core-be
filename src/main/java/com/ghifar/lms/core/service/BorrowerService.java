package com.ghifar.lms.core.service;

import com.ghifar.lms.core.entity.Borrower;
import com.ghifar.lms.core.repository.BorrowerRepository;
import com.ghifar.lms.core.dto.RegisterBorrowerRequest;
import com.ghifar.lms.core.dto.RegisterBorrowerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public RegisterBorrowerResponse register(RegisterBorrowerRequest request) {
        Borrower borrower = new Borrower();
        borrower.setName(request.name());
        borrower.setEmail(request.email());

        borrowerRepository.save(borrower);

        return mapToRegisterBorrowerResponse(borrower);
    }

    private RegisterBorrowerResponse mapToRegisterBorrowerResponse(Borrower borrower) {
        return new RegisterBorrowerResponse(
                borrower.getName(),
                borrower.getEmail()
        );
    }
}
