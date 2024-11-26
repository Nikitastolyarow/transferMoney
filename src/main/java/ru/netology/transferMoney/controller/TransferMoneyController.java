package ru.netology.transferMoney.controller;

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

    @Autowired
    private final TransferMoneyService transferMoneyService;

    public TransferMoneyController(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }


    //Обработка запроса на перевод денег
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest transferRequest) {
        try {
            String operationId = transferMoneyService.transfer(transferRequest);
            return ResponseEntity.ok(new TransferResponse(operationId));
        } catch (CardNotFoundException | InvalidAmountException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), 400));
        }
    }

    // Обработка подтверждения перевода
    @PostMapping("/confirmOperation")
    public ResponseEntity<?> confirmOperation(@RequestBody ConfirmOperationRequest request) {
        try {
            String operationId = transferMoneyService.confirmOperation(request);
            return ResponseEntity.ok(new TransferResponse(operationId));
        } catch (CardNotFoundException | InvalidAmountException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), 400));
        }
    }
}