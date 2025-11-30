package com.ghifar.lms.core.repository;

import com.ghifar.lms.core.entity.Book;
import com.ghifar.lms.core.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findFirstByBookAndReturnedAtIsNull(Book book);
}
