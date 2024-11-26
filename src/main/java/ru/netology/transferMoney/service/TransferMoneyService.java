package ru.netology.transferMoney.service;

import org.springframework.stereotype.Service;
import ru.netology.transferMoney.model.ConfirmOperationRequest;
import ru.netology.transferMoney.model.TransferRequest;

@Service
public class TransferMoneyService {
    public String transfer(TransferRequest transferRequest) {

        return "operationId";
    }

    public String confirmOperation(ConfirmOperationRequest request) {

        return "operationId";
    }
}
