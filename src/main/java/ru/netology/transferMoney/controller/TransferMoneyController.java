package ru.netology.transferMoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.transferMoney.model.ConfirmOperationRequest;
import ru.netology.transferMoney.model.TransferRequest;
import ru.netology.transferMoney.model.TransferResponse;
import ru.netology.transferMoney.service.TransferMoneyService;

@RestController
public class TransferMoneyController {

    private static final Logger log = LoggerFactory.getLogger(TransferMoneyController.class);

    private final TransferMoneyService transferMoneyService;

    @Autowired
    public TransferMoneyController(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }

    // Обработка запроса на перевод денег
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest) {
        log.info("Запрос на перевод получен. Карта отправителя: {}, Карта получателя: {}, Сумма: {} {}",
                transferRequest.getCardFromNumber(),
                transferRequest.getCardToNumber(),
                transferRequest.getAmount().getValue(),
                transferRequest.getAmount().getCurrency());

        String operationId = transferMoneyService.transfer(transferRequest);
        log.info("Перевод успешно завершён. ID операции: {}", operationId);

        return ResponseEntity.ok(new TransferResponse(operationId));
    }

    // Обработка подтверждения перевода
    @PostMapping("/confirmOperation")
    public ResponseEntity<TransferResponse> confirmOperation(@RequestBody ConfirmOperationRequest request) {
        log.info("Запрос на подтверждение операции. ID операции: {}", request.getOperationId());
        String operationId = transferMoneyService.confirmOperation(request);
        log.info("Операция подтверждена. ID операции: {}", operationId);

        return ResponseEntity.ok(new TransferResponse(operationId));
    }
}
