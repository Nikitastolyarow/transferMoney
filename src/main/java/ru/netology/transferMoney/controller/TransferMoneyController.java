package ru.netology.transferMoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.transferMoney.model.ConfirmOperationRequest;
import ru.netology.transferMoney.model.TransferRequest;
import ru.netology.transferMoney.model.TransferResponse;
import ru.netology.transferMoney.service.TransferMoneyService;

@CrossOrigin(origins = "https://serp-ya.github.io")
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
        // Если сумма передается в копейках, делаем конвертацию в рубли
        int amountInRubles = transferRequest.getAmount().getValue() / 100;

        log.info("Запрос на перевод получен. Карта отправителя: {}, Карта получателя: {}, Сумма: {} {}",
                transferRequest.getCardFromNumber(),
                transferRequest.getCardToNumber(),
                amountInRubles,  // Здесь выводим уже сумму в рублях
                transferRequest.getAmount().getCurrency());

        // Прокидываем в сервис сумму в рублях
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

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Все хорошо!");
    }
}
