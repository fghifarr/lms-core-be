package com.ghifar.lms.core.service;

import com.ghifar.lms.core.common.exception.ValidationException;
import com.ghifar.lms.core.dto.RegisterBookRequest;
import com.ghifar.lms.core.dto.BookResponse;
import com.ghifar.lms.core.entity.Book;
import com.ghifar.lms.core.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @Test
    public void register_withValidRequestAndNewBook_shouldReturnResponse() {
        RegisterBookRequest request = new RegisterBookRequest(
                "Buku Ghifar",
                "ISBN-0001",
                "F Ghifar R"
        );

        Book book = generateDefaultBook(request);

        when(bookRepository.findFirstByIsbn(anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = bookService.register(request);

        assertNotNull(response);
        assertEquals(request.title(), response.title());
        assertEquals(request.isbn(), response.isbn());
        assertEquals(request.author(), response.author());
        assertNotNull(response.id());
        verify(bookRepository, times(1)).findFirstByIsbn(anyString());
        verify(bookRepository, times(1)).save(bookCaptor.capture());

        Book toBeSavedBook = bookCaptor.getValue();
        assertEquals(request.title(), toBeSavedBook.getTitle());
        assertEquals(request.isbn(), toBeSavedBook.getIsbn());
        assertEquals(request.author(), toBeSavedBook.getAuthor());
        assertEquals(Book.Status.AVAILABLE, toBeSavedBook.getStatus());
    }

    @Test
    public void register_withValidRequestAndAlreadyExistBook_shouldReturnResponse() {
        RegisterBookRequest request = new RegisterBookRequest(
                "Buku Ghifar",
                "ISBN-0001",
                "F Ghifar R"
        );

        Book existingBook = generateDefaultBook(request);

        Book book = generateDefaultBook(request);
        book.setId(book.getId() + 1L);

        when(bookRepository.findFirstByIsbn(anyString())).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = bookService.register(request);

        assertNotNull(response);
        assertEquals(request.title(), response.title());
        assertEquals(request.isbn(), response.isbn());
        assertEquals(request.author(), response.author());
        assertNotNull(response.id());
        verify(bookRepository, times(1)).findFirstByIsbn(anyString());
        verify(bookRepository, times(1)).save(bookCaptor.capture());

        Book toBeSavedBook = bookCaptor.getValue();
        assertEquals(request.title(), toBeSavedBook.getTitle());
        assertEquals(request.isbn(), toBeSavedBook.getIsbn());
        assertEquals(request.author(), toBeSavedBook.getAuthor());
        assertEquals(Book.Status.AVAILABLE, toBeSavedBook.getStatus());
    }

    @Test
    public void register_withExistingIsbnAndTitleNotMatch_shouldThrowValidationException() {
        RegisterBookRequest request = new RegisterBookRequest(
                "Buku Ghifar",
                "ISBN-0001",
                "F Ghifar R"
        );

        Book existingBook = generateDefaultBook(request);
        existingBook.setTitle(existingBook.getTitle() + " VERSION 2");

        when(bookRepository.findFirstByIsbn(anyString())).thenReturn(Optional.of(existingBook));

        ValidationException ex = assertThrows(ValidationException.class, () -> bookService.register(request));

        assertNotNull(ex);
        assertEquals("Books with same ISBN should have same title and author", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    private Book generateDefaultBook(RegisterBookRequest request) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setAuthor(request.author());
        book.setStatus(Book.Status.AVAILABLE);

        return book;
    }
}
