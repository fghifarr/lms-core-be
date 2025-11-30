package com.ghifar.lms.core.service;

import com.ghifar.lms.core.common.exception.ValidationException;
import com.ghifar.lms.core.dto.BorrowBookRequest;
import com.ghifar.lms.core.dto.LoanResponse;
import com.ghifar.lms.core.dto.ReturnBookRequest;
import com.ghifar.lms.core.entity.Book;
import com.ghifar.lms.core.entity.Borrower;
import com.ghifar.lms.core.entity.Loan;
import com.ghifar.lms.core.repository.BookRepository;
import com.ghifar.lms.core.repository.BorrowerRepository;
import com.ghifar.lms.core.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    @Transactional
    public LoanResponse borrowBook(BorrowBookRequest request) {
        Book book = bookRepository.findById(request.bookId()).orElseThrow(
                () -> new ValidationException(HttpStatus.NOT_FOUND, "Couldn't find book with id : " + request.bookId())
        );
        if (!Book.Status.AVAILABLE.equals(book.getStatus())) {
            log.info("book is not available. book: {}, status: {}", book.getId(), book.getStatus());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Book is not available. bookId: " + book.getId());
        }
        Borrower borrower = borrowerRepository.findById(request.borrowerId()).orElseThrow(
                () -> new ValidationException(HttpStatus.NOT_FOUND, "Couldn't find borrower with id : " + request.borrowerId())
        );

        book.setStatus(Book.Status.BORROWED);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBorrower(borrower);
        loan.setBook(book);
        loan.setBorrowedAt(Instant.now());

        loanRepository.save(loan);

        return mapToBorrowBookResponse(book, borrower, loan);
    }

    @Transactional
    public LoanResponse returnBook(ReturnBookRequest request) {
        Book book = bookRepository.findById(request.bookId()).orElseThrow(
                () -> new ValidationException(HttpStatus.NOT_FOUND, "Couldn't find book with id : " + request.bookId())
        );
        if (!Book.Status.BORROWED.equals(book.getStatus())) {
            log.info("book is not borrowed. book: {}, status: {}", book.getId(), book.getStatus());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Book is not borrowed. bookId: " + request.bookId());
        }
        Borrower returnee = borrowerRepository.findById(request.borrowerId()).orElseThrow(
                () -> new ValidationException(HttpStatus.NOT_FOUND, "Couldn't find borrower with id : " + request.borrowerId())
        );
        Loan loan = loanRepository.findFirstByBookAndReturnedAtIsNull(book).orElseThrow(
                () -> new ValidationException(HttpStatus.BAD_REQUEST, "Couldn't find active loan for book with id: " + book.getId())
        );
        Borrower loanBorrower = loan.getBorrower();
        if (!returnee.getId().equals(loanBorrower.getId())) {
            log.info("returnee: {}, does not match with loan borrower: {}", returnee.getId(), loanBorrower.getId());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Returnee should be the one who borrow the book");
        }

        book.setStatus(Book.Status.AVAILABLE);
        bookRepository.save(book);

        loan.setReturnedAt(Instant.now());
        loanRepository.save(loan);

        return mapToBorrowBookResponse(book, returnee, loan);
    }

    private LoanResponse mapToBorrowBookResponse(Book book, Borrower borrower, Loan loan) {
        return new LoanResponse(
                new LoanResponse.Book(
                        book.getId(),
                        book.getIsbn(),
                        book.getTitle()
                ),
                new LoanResponse.Borrower(
                        borrower.getId(),
                        borrower.getName(),
                        borrower.getEmail()
                ),
                loan.getBorrowedAt(),
                loan.getReturnedAt()
        );
    }
}
