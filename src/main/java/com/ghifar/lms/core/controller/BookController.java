package com.ghifar.lms.core.controller;

import com.ghifar.lms.core.dto.RegisterBookRequest;
import com.ghifar.lms.core.dto.BookResponse;
import com.ghifar.lms.core.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> register(@RequestBody @Valid RegisterBookRequest request) {
        return ResponseEntity.ok(bookService.register(request));
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(bookService.getList(page, size));
    }
}
