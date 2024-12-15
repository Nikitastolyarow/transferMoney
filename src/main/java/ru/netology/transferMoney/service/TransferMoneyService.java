package ru.netology.transferMoney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netology.transferMoney.exception.CardNotFoundException;
import ru.netology.transferMoney.exception.InvalidAmountException;
import ru.netology.transferMoney.model.Card;
import ru.netology.transferMoney.model.ConfirmOperationRequest;
import ru.netology.transferMoney.model.TransferRequest;
import ru.netology.transferMoney.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service

public class TransferMoneyService {

    private static final Logger log = LoggerFactory.getLogger(TransferMoneyService.class);

    @Autowired
    private CardRepository cardRepository;

    public String transfer(TransferRequest transferRequest) {
        log.info("Начало обработки перевода: {}", transferRequest);

        // Проверка карты отправителя
        Card cardFrom = cardRepository.findCardByNumber(transferRequest.getCardFromNumber());
        if (cardFrom == null) {
            log.error("Карта отправителя не найдена: {}", transferRequest.getCardFromNumber());
            throw new CardNotFoundException("Карта отправителя не найдена");
        }

        // Проверка карты получателя
        Card cardTo = cardRepository.findCardByNumber(transferRequest.getCardToNumber());
        if (cardTo == null) {
            log.error("Карта получателя не найдена: {}", transferRequest.getCardToNumber());
            throw new CardNotFoundException("Карта получателя не найдена");
        }

        // Проверка баланса отправителя
        int transferAmount = Integer.parseInt(transferRequest.getAmount().getValue());
        if (cardFrom.getBalance() < transferAmount) {
            log.error("Недостаточно средств на карте: {}", transferRequest.getCardFromNumber());
            throw new IllegalArgumentException("Недостаточно средств для перевода");
        }

        // Проверка CVV отправителя
        if (!cardFrom.getCardCVV().equals(transferRequest.getCardFromCVV())) {
            log.error("Неверный CVV для карты: {}", transferRequest.getCardFromNumber());
            throw new InvalidAmountException("Неверный CVV отправителя");
        }

        // Проверка срока действия карты
        if (!cardFrom.getСardValidUntil().equals(transferRequest.getCardFromValidTill())) {
            log.error("Неверный срок действия карты: {}", transferRequest.getCardFromNumber());
            throw new InvalidAmountException("Неверно указан срок действия карты отправителя");
        }

        // Выполнение перевода
        cardFrom.setBalance(cardFrom.getBalance() - transferAmount);
        cardTo.setBalance(cardTo.getBalance() + transferAmount);
        log.info("Перевод выполнен: с карты {} на карту {} сумма {}", cardFrom.getCardNumber(), cardTo.getCardNumber(), transferAmount);

        // Логирование операции
        logOperation(cardFrom.getCardNumber(), cardTo.getCardNumber(), transferAmount);

        String operationId = generateOperationId();
        log.info("Операция завершена успешно. ID операции: {}", operationId);
        return operationId;
    }

    public String confirmOperation(ConfirmOperationRequest request) {
        log.info("Подтверждение операции с ID: {}", request.getOperationId());
        // Здесь логика подтверждения операции
        log.info("Операция {} успешно подтверждена", request.getOperationId());
        return request.getOperationId();
    }

    private void logOperation(String CardFrom, String CardTo , int amount) {
        try{
            FileWriter writer = new FileWriter("transactions.log");
            writer.write(LocalDateTime.now() + " | " +
                        "Отправитель" + CardFrom + " " +
                        "Получатель " + CardTo + " " +
                        "Сумма: " + amount + " RUB\n");
            writer.flush();  // Обязательно сохраняет запись в файл
            log.info("Запись успешно добавлена в transactions.log");
        }catch (IOException e) {
            log.error("Ошибка записи в лог файл: {}", e.getMessage());
        }
    }
    private String generateOperationId() {
        return java.util.UUID.randomUUID().toString(); // Уникальный ID операции
    }
}
