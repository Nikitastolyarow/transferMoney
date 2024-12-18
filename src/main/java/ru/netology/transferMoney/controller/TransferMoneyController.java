package ru.netology.transferMoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.transferMoney.exception.CardNotFoundException;
import ru.netology.transferMoney.exception.InvalidAmountException;
import ru.netology.transferMoney.model.ConfirmOperationRequest;
import ru.netology.transferMoney.model.ErrorResponse;
import ru.netology.transferMoney.model.TransferRequest;
import ru.netology.transferMoney.model.TransferResponse;
import ru.netology.transferMoney.service.TransferMoneyService;

@RestController
public class TransferMoneyController {

    private static final Logger log = LoggerFactory.getLogger(TransferMoneyController.class);

    @Autowired
    private final TransferMoneyService transferMoneyService;

    public TransferMoneyController(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }

    // Обработка запроса на перевод денег
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest transferRequest) {
        log.info("Запрос на перевод получен: {}", transferRequest);
        try {
            String operationId = transferMoneyService.transfer(transferRequest);
            log.info("Перевод успешно завершён. ID операции: {}", operationId);
            return ResponseEntity.ok(new TransferResponse(operationId));
        } catch (CardNotFoundException | InvalidAmountException | IllegalArgumentException e) {
            log.warn("Ошибка при выполнении перевода: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), 400));
        }
    }

    // Обработка подтверждения перевода
    @PostMapping("/confirmOperation")
    public ResponseEntity<?> confirmOperation(@RequestBody ConfirmOperationRequest request) {
        log.info("Запрос на подтверждение операции получен: {}", request);
        try {
            String operationId = transferMoneyService.confirmOperation(request);
            log.info("Операция подтверждена. ID операции: {}", operationId);
            return ResponseEntity.ok(new TransferResponse(operationId));
        } catch (CardNotFoundException | InvalidAmountException e) {
            log.warn("Ошибка при подтверждении операции: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), 400));
        }
    }
}
