package com.ghifar.lms.core.service;

import com.ghifar.lms.core.dto.RegisterBorrowerRequest;
import com.ghifar.lms.core.dto.RegisterBorrowerResponse;
import com.ghifar.lms.core.entity.Borrower;
import com.ghifar.lms.core.repository.BorrowerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowerServiceTest {

    @InjectMocks
    private BorrowerService borrowerService;

    @Mock
    private BorrowerRepository borrowerRepository;

    @Captor
    private ArgumentCaptor<Borrower> borrowerCaptor;

    @Test
    public void register_withValidRequest_shouldReturnResponse() {
        RegisterBorrowerRequest request = new RegisterBorrowerRequest(
                "Ghifar",
                "fghifarr@gmail.com"
        );

        Borrower borrower = new Borrower();
        borrower.setId(1);
        borrower.setName(request.name());
        borrower.setEmail(request.email());

        when(borrowerRepository.save(any(Borrower.class))).thenReturn(borrower);

        RegisterBorrowerResponse response = borrowerService.register(request);

        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.email(), response.email());
        verify(borrowerRepository, times(1)).save(borrowerCaptor.capture());

        Borrower toBeSavedBorrower = borrowerCaptor.getValue();
        assertEquals(request.name(), toBeSavedBorrower.getName());
        assertEquals(request.email(), toBeSavedBorrower.getEmail());
    }
}
