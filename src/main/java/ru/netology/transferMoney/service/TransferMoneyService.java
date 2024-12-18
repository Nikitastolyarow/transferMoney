package ru.netology.transferMoney.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netology.transferMoney.exception.CardNotFoundException;
import ru.netology.transferMoney.exception.InvalidAmountException;
import ru.netology.transferMoney.model.Card;
import ru.netology.transferMoney.model.ConfirmOperationRequest;
import ru.netology.transferMoney.model.TransferRequest;
import ru.netology.transferMoney.repository.CardRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class TransferMoneyService {

    private static final Logger log = LoggerFactory.getLogger(TransferMoneyService.class);

    @Autowired
    private CardRepository cardRepository;

    public String transfer(TransferRequest transferRequest) {
        // Извлекаем данные перевода
        int transferAmount = Integer.parseInt(transferRequest.getAmount().getValue());
        int commission = calculateCommission(transferAmount); // Расчёт комиссии

        // Проверка отправителя
        Card cardFrom = cardRepository.findCardByNumber(transferRequest.getCardFromNumber());
        if (cardFrom == null) {
            throw new CardNotFoundException("Карта отправителя не найдена");
        }

        // Проверка получателя
        Card cardTo = cardRepository.findCardByNumber(transferRequest.getCardToNumber());
        if (cardTo == null) {
            throw new CardNotFoundException("Карта получателя не найдена");
        }

        // Проверка баланса отправителя
        if (cardFrom.getBalance() < (transferAmount + commission)) {
            throw new IllegalArgumentException("Недостаточно средств для перевода с учётом комиссии");
        }

        // Выполнение перевода
        cardFrom.setBalance(cardFrom.getBalance() - (transferAmount + commission));
        cardTo.setBalance(cardTo.getBalance() + transferAmount);

        // Генерация ID операции
        String operationId = generateOperationId();

        // Логирование операции
        logOperation(cardFrom.getCardNumber(), cardTo.getCardNumber(), transferAmount, commission, operationId, "Успешно");

        return operationId;
    }

    public String confirmOperation(ConfirmOperationRequest request) {
        log.info("Подтверждение операции с ID: {}", request.getOperationId());
        // Здесь логика подтверждения операции
        log.info("Операция {} успешно подтверждена", request.getOperationId());
        return request.getOperationId();
    }

    private void logOperation(String cardFrom, String cardTo, int amount, int commission, String operationId, String status) {
        String logEntry = String.format(
                "%s | Отправитель: %s " +
                        "| Получатель: %s " +
                        "| Сумма: %d RUB " +
                        "| Комиссия: %d RUB " +
                        "| Статус: %s " +
                        "| ID операции: %s\n",
                LocalDateTime.now(), cardFrom, cardTo, amount, commission, status, operationId
        );

        try (FileWriter writer = new FileWriter("transactions.log", true)) { // Дозапись в файл
            writer.write(logEntry);
            writer.flush();
            log.info("Запись успешно добавлена в transactions.log");
        } catch (IOException e) {
            log.error("Ошибка записи в лог файл: {}", e.getMessage());
        }
    }

    private int calculateCommission(int amount) {
        return (int) (amount * 0.01); // Комиссия 1%
    }

    private String generateOperationId() {
        return java.util.UUID.randomUUID().toString(); // Уникальный ID операции
    }
}
