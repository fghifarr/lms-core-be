package com.ghifar.lms.core.controller;

import com.ghifar.lms.core.dto.BookResponse;
import com.ghifar.lms.core.dto.ErrorResponse;
import com.ghifar.lms.core.dto.RegisterBorrowerRequest;
import com.ghifar.lms.core.dto.RegisterBorrowerResponse;
import com.ghifar.lms.core.service.BorrowerService;
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

@Tag(name = "Borrower Management", description = "APIS for managing people that comes to library to borrow books (borrowers)")
@RestController
@RequestMapping("/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @Operation(
            summary = "Register a new borrower",
            description = "Register a new borrower. Email must be unique"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully registered the borrower",
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
    public ResponseEntity<RegisterBorrowerResponse> register(@RequestBody @Valid RegisterBorrowerRequest request) {
        return ResponseEntity.ok(borrowerService.register(request));
    }
}
