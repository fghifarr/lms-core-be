package com.ghifar.lms.core.repository;

import com.ghifar.lms.core.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Integer> {
}
