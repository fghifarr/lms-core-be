package com.ghifar.lms.core.controller;

import com.ghifar.lms.core.dto.BorrowBookRequest;
import com.ghifar.lms.core.dto.LoanResponse;
import com.ghifar.lms.core.dto.ReturnBookRequest;
import com.ghifar.lms.core.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("borrow-book")
    public ResponseEntity<LoanResponse> borrowBook(@RequestBody @Valid BorrowBookRequest request) {
        return ResponseEntity.ok(loanService.borrowBook(request));
    }

    @PostMapping("return-book")
    public ResponseEntity<LoanResponse> returnBook(@RequestBody @Valid ReturnBookRequest request) {
        return ResponseEntity.ok(loanService.returnBook(request));
    }
}
