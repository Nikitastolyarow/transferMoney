package ru.netology.transferMoney.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Amount {
    private final int value;   // сумма
    private final String currency;  // валюта
}
