package com.ghifar.lms.core.controller;

import com.ghifar.lms.core.dto.RegisterBorrowerRequest;
import com.ghifar.lms.core.dto.RegisterBorrowerResponse;
import com.ghifar.lms.core.service.BorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping
    public ResponseEntity<RegisterBorrowerResponse> register(@RequestBody @Valid RegisterBorrowerRequest request) {
        return ResponseEntity.ok(borrowerService.register(request));
    }
}
