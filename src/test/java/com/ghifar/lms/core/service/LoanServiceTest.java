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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;
    @Captor
    private ArgumentCaptor<Loan> loanCaptor;

    @Test
    public void borrowBook_withValidRequest_shouldReturnResponse() {
        BorrowBookRequest request = new BorrowBookRequest(
                1L,
                1
        );
        Book book = generateDefaultBook();
        Borrower borrower = generateDefaultBorrower();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(anyInt())).thenReturn(Optional.of(borrower));

        LoanResponse response = loanService.borrowBook(request);

        assertNotNull(response);
        assertEquals(book.getId(), response.book().id());
        assertEquals(borrower.getId(), response.borrower().id());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(borrowerRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        verify(loanRepository, times(1)).save(loanCaptor.capture());

        Book borrowedBook = bookCaptor.getValue();
        assertEquals(book.getId(), borrowedBook.getId());
        assertEquals(Book.Status.BORROWED, borrowedBook.getStatus());

        Loan borrowLoan = loanCaptor.getValue();
        assertEquals(book.getId(), borrowLoan.getBook().getId());
        assertEquals(borrower.getId(), borrowLoan.getBorrower().getId());
        assertNotNull(borrowLoan.getBorrowedAt());
        assertNull(borrowLoan.getReturnedAt());
    }

    @Test
    public void borrowBook_withInvalidBookId_shouldReturnNotFound() {
        BorrowBookRequest request = new BorrowBookRequest(
                1L,
                1
        );

        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> loanService.borrowBook(request));

        assertNotNull(ex);
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Couldn't find book with id : " + request.bookId(), ex.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void borrowBook_withBookNotAvailable_shouldReturnBadRequest() {
        BorrowBookRequest request = new BorrowBookRequest(
                1L,
                1
        );
        Book book = generateDefaultBook();
        book.setStatus(Book.Status.NOT_AVAILABLE);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        ValidationException ex = assertThrows(ValidationException.class, () -> loanService.borrowBook(request));

        assertNotNull(ex);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Book is not available. bookId: " + request.borrowerId(), ex.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void borrowBook_withInvalidBorrowerId_shouldReturnNotFound() {
        BorrowBookRequest request = new BorrowBookRequest(
                1L,
                1
        );
        Book book = generateDefaultBook();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(anyInt())).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> loanService.borrowBook(request));

        assertNotNull(ex);
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Couldn't find borrower with id : " + request.borrowerId(), ex.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(borrowerRepository, times(1)).findById(anyInt());
    }

    @Test
    public void returnBook_withValidRequest_shouldReturnResponse() {
        ReturnBookRequest request = new ReturnBookRequest(
                1L,
                1
        );
        Book book = generateDefaultBook();
        book.setStatus(Book.Status.BORROWED);
        Borrower borrower = generateDefaultBorrower();
        Loan borrowLoan = generateBorrowLoan(book, borrower);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(anyInt())).thenReturn(Optional.of(borrower));
        when(loanRepository.findFirstByBookAndReturnedAtIsNull(any(Book.class))).thenReturn(Optional.of(borrowLoan));

        LoanResponse response = loanService.returnBook(request);

        assertNotNull(response);
        assertEquals(book.getId(), response.book().id());
        assertEquals(borrower.getId(), response.borrower().id());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(borrowerRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        verify(loanRepository, times(1)).save(loanCaptor.capture());

        Book borrowedBook = bookCaptor.getValue();
        assertEquals(book.getId(), borrowedBook.getId());
        assertEquals(Book.Status.AVAILABLE, borrowedBook.getStatus());

        Loan returnLoan = loanCaptor.getValue();
        assertEquals(book.getId(), returnLoan.getBook().getId());
        assertEquals(borrower.getId(), returnLoan.getBorrower().getId());
        assertNotNull(returnLoan.getBorrowedAt());
        assertNotNull(returnLoan.getReturnedAt());
    }

    @Test
    public void returnBook_withInvalidBookId_shouldReturnNotFound() {
        ReturnBookRequest request = new ReturnBookRequest(
                1L,
                1
        );

        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> loanService.returnBook(request));

        assertNotNull(ex);
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Couldn't find book with id : " + request.bookId(), ex.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void returnBook_withAvailableBook_shouldReturnBadRequest() {
        ReturnBookRequest request = new ReturnBookRequest(
                1L,
                1
        );
        Book book = generateDefaultBook();
        book.setStatus(Book.Status.AVAILABLE);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        ValidationException ex = assertThrows(ValidationException.class, () -> loanService.returnBook(request));

        assertNotNull(ex);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals(String.format("Book is not borrowed. bookId: %s", request.bookId()), ex.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void returnBook_withInvalidBorrowerId_shouldReturnNotFound() {
        ReturnBookRequest request = new ReturnBookRequest(
                1L,
                1
        );
        Book book = generateDefaultBook();
        book.setStatus(Book.Status.BORROWED);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(anyInt())).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> loanService.returnBook(request));

        assertNotNull(ex);
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Couldn't find borrower with id : " + request.borrowerId(), ex.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(borrowerRepository, times(1)).findById(anyInt());
    }

    @Test
    public void returnBook_withActiveLoanNotFound_shouldReturnBadRequest() {
        ReturnBookRequest request = new ReturnBookRequest(
                1L,
                1
        );
        Book book = generateDefaultBook();
        book.setStatus(Book.Status.BORROWED);
        Borrower borrower = generateDefaultBorrower();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(anyInt())).thenReturn(Optional.of(borrower));
        when(loanRepository.findFirstByBookAndReturnedAtIsNull(any(Book.class))).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> loanService.returnBook(request));

        assertNotNull(ex);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Couldn't find active loan for book with id: " + book.getId(), ex.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(borrowerRepository, times(1)).findById(anyInt());
        verify(loanRepository, times(1)).findFirstByBookAndReturnedAtIsNull(any(Book.class));
    }

    @Test
    public void returnBook_withReturneeDifferentWithBorrower_shouldReturnBadRequest() {
        ReturnBookRequest request = new ReturnBookRequest(
                1L,
                1
        );
        Book book = generateDefaultBook();
        book.setStatus(Book.Status.BORROWED);
        Borrower returnee = generateDefaultBorrower();
        Borrower borrower = generateDefaultBorrower();
        borrower.setId(99);
        Loan borrowLoan = generateBorrowLoan(book, borrower);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(borrowerRepository.findById(anyInt())).thenReturn(Optional.of(returnee));
        when(loanRepository.findFirstByBookAndReturnedAtIsNull(any(Book.class))).thenReturn(Optional.of(borrowLoan));

        ValidationException ex = assertThrows(ValidationException.class, () -> loanService.returnBook(request));

        assertNotNull(ex);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals(String.format("Returnee should be the one who borrow the book"), ex.getMessage());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(borrowerRepository, times(1)).findById(anyInt());
        verify(loanRepository, times(1)).findFirstByBookAndReturnedAtIsNull(any(Book.class));
    }

    public Book generateDefaultBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Buku Ghifar");
        book.setIsbn("ISBN-001");
        book.setAuthor("F Ghifar R");
        book.setStatus(Book.Status.AVAILABLE);

        return book;
    }

    public Borrower generateDefaultBorrower() {
        Borrower borrower = new Borrower();
        borrower.setId(1);
        borrower.setName("Ghifar");
        borrower.setEmail("fghifarr@gmail.com");

        return borrower;
    }

    public Loan generateBorrowLoan(Book book, Borrower borrower) {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBorrower(borrower);
        loan.setBook(book);
        loan.setBorrowedAt(Instant.now());

        return loan;
    }
}
