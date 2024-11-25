package ru.netology.transferMoney.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferMoneyController {
    private final TransferMoneyController transferMoneyController;

    @Autowired
    public TransferMoneyController(TransferMoneyController transferMoneyController) {
        this.transferMoneyController = transferMoneyController;
    }

    //Обработка запроса на перевод денег
    @PostMapping("/transfer")

    }
    // Обработка подтверждения перевода
    @PostMapping("/confirmOperation")

}
