
package ru.netology.transferMoney.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.netology.transferMoney.model.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void testHandleCardNotFoundException() {

        CardNotFoundException exception = new  CardNotFoundException("Карта не найдена");

        ResponseEntity<ErrorResponse> response = handler.handleCardNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Карта не найдена", response.getBody().getMessage());
        assertEquals(400, response.getBody().getId());

    }
}
