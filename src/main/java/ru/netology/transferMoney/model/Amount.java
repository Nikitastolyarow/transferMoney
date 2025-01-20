package ru.netology.transferMoney.model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Amount {
    private int value;   //сумма
    private String currency;  //валюта
}
