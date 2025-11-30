package com.ghifar.lms.core.repository;

import com.ghifar.lms.core.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findFirstByIsbn(String isbn);
    Page<Book> findAll(Pageable pageable);
}
