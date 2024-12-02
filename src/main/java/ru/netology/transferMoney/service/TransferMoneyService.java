package ru.netology.transferMoney.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import ru.netology.transferMoney.exception.CardNotFoundException;
import ru.netology.transferMoney.exception.InvalidAmountException;
import ru.netology.transferMoney.model.Card;
import ru.netology.transferMoney.model.ConfirmOperationRequest;
import ru.netology.transferMoney.model.TransferRequest;
import ru.netology.transferMoney.repository.CardRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service

public class TransferMoneyService {

    @Autowired
    private CardRepository cardRepository;

    public String transfer(TransferRequest transferRequest) {
        //проверка карты отправителя
        Card cardFrom = cardRepository.findCardByNumber(transferRequest.getCardFromNumber());
        if (cardFrom == null) {
            throw new CardNotFoundException("Карта отправителя не найдена");
        }
        //проверка карты получателя
        Card cardTo = cardRepository.findCardByNumber(transferRequest.getCardToNumber());
        if (cardTo == null) {
            throw new CardNotFoundException("Карта получателя не найдена");
        }
        // проверка баланса отправителя
        int tranferAmount = Integer.parseInt(transferRequest.getAmount().getValue());
        if(cardFrom.getBalance()< tranferAmount) {
            throw new IllegalArgumentException("Недостаточно средств для перевода");
        }
        // провекра CVV отправителя
        if ( cardFrom.getCardCVV().equals(transferRequest.getCardFromCVV())) {
            throw new InvalidAmountException("Неверный CVV отправителя");
        }
        //проверка срока действия карты отправителя
        if(cardTo.getСardValidUntil().equals(transferRequest.getCardFromValidTill())){
            throw new InvalidAmountException("Неверно указан срок действия карты отправителя");
        }
        // Выполнение перевода: списание с карты отправителя
         cardFrom.setBalance(cardFrom.getBalance() - tranferAmount);

        // Зачисление на карту получателя
         cardTo.setBalance(cardTo.getBalance() + tranferAmount);

         logOperation(cardFrom.getCardNumber(), cardTo.getCardNumber() , tranferAmount);
        String operationId = generateOperationId();
         return operationId;
    }

//    public String confirmOperation(ConfirmOperationRequest request) {
//
//        return "operationId";
//    }

    private void logOperation(String CardFrom, String CardTo , int amount) {
        try{
            FileWriter writer = new FileWriter("transactions.log");
            writer.write(LocalDateTime.now() + " | " +
                        "Отправитель" + CardFrom + " " +
                        "Получатель " + CardTo + " " +
                        "Сумма: " + amount + " RUB\n");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String generateOperationId() {
        return java.util.UUID.randomUUID().toString(); // Уникальный ID операции
    }
}
