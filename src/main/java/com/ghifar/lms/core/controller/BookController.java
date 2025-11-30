package com.ghifar.lms.core.controller;

import com.ghifar.lms.core.dto.ErrorResponse;
import com.ghifar.lms.core.dto.RegisterBookRequest;
import com.ghifar.lms.core.dto.BookResponse;
import com.ghifar.lms.core.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Book Management", description = "APIS for managing books in the library")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Register a new book",
            description = "Register a new book. Book with same ISBN cannot have different title n name"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully registered the book",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    @PostMapping
    public ResponseEntity<BookResponse> register(@RequestBody @Valid RegisterBookRequest request) {
        return ResponseEntity.ok(bookService.register(request));
    }


    @Operation(
            summary = "Get book list",
            description = "Get the book list with pagination. Default page = 0, size = 20"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully registered the book",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))
                    ),
            }
    )
    @GetMapping
    public ResponseEntity<Page<BookResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(bookService.getList(page, size));
    }
}
