package com.ghifar.lms.core.controller;

import com.ghifar.lms.core.dto.*;
import com.ghifar.lms.core.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Loan Management", description = "APIS for managing loans which basically a borrow/return records")
@RestController
@RequestMapping("loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @Operation(
            summary = "Borrow a book",
            description = "Borrow a book. The book must be AVAILABLE. This API also should prevent concurrent request"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully borrowed the book",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflicted. Most likely due to optimistic locking. " +
                                    "The retry mechanism already implemented but have limit retry (4 times)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    @PostMapping("borrow-book")
    public ResponseEntity<LoanResponse> borrowBook(@RequestBody @Valid BorrowBookRequest request) {
        return ResponseEntity.ok(loanService.borrowBook(request));
    }

    @Operation(
            summary = "Return a book",
            description = "Borrow a book. The book must be BORROWED. This API also should prevent concurrent request." +
                    " The returnee should be the same with the people who borrow the book"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully borrowed the book",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflicted. Most likely due to optimistic locking. " +
                                    "The retry mechanism already implemented but have limit retry (4 times)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    @PostMapping("return-book")
    public ResponseEntity<LoanResponse> returnBook(@RequestBody @Valid ReturnBookRequest request) {
        return ResponseEntity.ok(loanService.returnBook(request));
    }
}
