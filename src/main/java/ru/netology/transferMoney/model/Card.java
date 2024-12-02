package ru.netology.transferMoney.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {
    private String cardNumber;
    private String сardValidUntil;
    private String cardCVV;
    private Integer balance;
}
