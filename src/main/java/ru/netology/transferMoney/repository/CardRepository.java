package ru.netology.transferMoney.repository;

import ru.netology.transferMoney.model.Card;

public interface CardRepository {
    Card findCardByNumber(String cardNumber);
    void updateCardBalance(String cardNumber, Integer newBalance);
}
