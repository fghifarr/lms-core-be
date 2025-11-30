package com.ghifar.lms.core.service;

import com.ghifar.lms.core.common.exception.ValidationException;
import com.ghifar.lms.core.dto.RegisterBookRequest;
import com.ghifar.lms.core.dto.BookResponse;
import com.ghifar.lms.core.entity.Book;
import com.ghifar.lms.core.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponse register(RegisterBookRequest request) {
        Optional<Book> sameBookOpt = bookRepository.findFirstByIsbn(request.isbn());
        sameBookOpt.ifPresent(sameBook -> {
            if (!sameBook.getTitle().equals(request.title()) || !sameBook.getAuthor().equals(request.author())) {
                throw new ValidationException(HttpStatus.BAD_REQUEST, "Books with same ISBN should have same title and author");
            }
        });

        Book book = new Book();
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setAuthor(request.author());
        book.setStatus(Book.Status.AVAILABLE);

        Book savedBook = bookRepository.save(book);

        return mapToRegisterBookResponse(savedBook);
    }

    public Page<BookResponse> getList(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")))
                .map(this::mapToRegisterBookResponse);
    }

    private BookResponse mapToRegisterBookResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthor(),
                book.getStatus().name()
        );
    }
}
