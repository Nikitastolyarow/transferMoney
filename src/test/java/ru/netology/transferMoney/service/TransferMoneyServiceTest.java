
package ru.netology.transferMoney.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.netology.transferMoney.exception.CardNotFoundException;
import ru.netology.transferMoney.model.Amount;
import ru.netology.transferMoney.model.Card;
import ru.netology.transferMoney.model.TransferRequest;
import ru.netology.transferMoney.repository.CardRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransferMoneyServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransferMoneyService transferMoneyService;

    public TransferMoneyServiceTest() {
        MockitoAnnotations.openMocks(this); // Инициализация mock-объектов
    }

    @Test
    public void testSuccessfulTransfer() {
        // Arrange
        String cardFromNumber = "1111222233334444";
        String cardToNumber = "5555666677778888";
        int initialBalanceFrom = 10000;
        int initialBalanceTo = 5000;
        int transferAmount = 2000;
        int commission = (int) (transferAmount * 0.01);

        Card cardFrom = new Card(cardFromNumber, "12/24", "123", initialBalanceFrom);
        Card cardTo = new Card(cardToNumber, "11/25", "456", initialBalanceTo);

        when(cardRepository.findCardByNumber(cardFromNumber)).thenReturn(cardFrom);
        when(cardRepository.findCardByNumber(cardToNumber)).thenReturn(cardTo);

        TransferRequest transferRequest = new TransferRequest(
                cardFromNumber, "12/24", "123", cardToNumber, new Amount(transferAmount, "RUB")
        );

        // Act
        String operationId = transferMoneyService.transfer(transferRequest);

        // Assert
        assertNotNull(operationId);
        assertEquals(initialBalanceFrom - transferAmount - commission, cardFrom.getBalance());
        assertEquals(initialBalanceTo + transferAmount, cardTo.getBalance());
        verify(cardRepository, times(1)).findCardByNumber(cardFromNumber);
        verify(cardRepository, times(1)).findCardByNumber(cardToNumber);
    }


    @Test
    public void testCardFromNotFound(){
        String cardFromNumber = "1111222233334444";
        String cardToNumber = "5555666677778888";

        when(cardRepository.findCardByNumber(cardFromNumber)).thenReturn(null);

        TransferRequest transferRequest = new TransferRequest(
                cardFromNumber, "12/24", "123", cardToNumber,new Amount(2000, "RUB")
        );

        assertThrows(CardNotFoundException.class, () -> transferMoneyService.transfer(transferRequest));
        verify(cardRepository, times(1)).findCardByNumber(cardFromNumber);

    }

    @Test
    public void testInsufficientFunds(){

        String cardFromNumber = "1111222233334444";
        String cardToNumber = "5555666677778888";

        Card cardFrom = new Card(cardFromNumber, "12/24", "123", 1000);
        Card cardTo = new Card(cardToNumber, "11/25", "456", 3000);

        when(cardRepository.findCardByNumber(cardFromNumber)).thenReturn(cardFrom);
        when(cardRepository.findCardByNumber(cardToNumber)).thenReturn(cardTo);

        TransferRequest transferRequest = new TransferRequest(
                cardFromNumber, "12/24", "123", cardToNumber,new Amount(2000, "RUB")
        );

        assertThrows(IllegalArgumentException.class, () -> transferMoneyService.transfer(transferRequest));
        verify(cardRepository, times(1)).findCardByNumber(cardFromNumber);
        verify(cardRepository, times(1)).findCardByNumber(cardToNumber);
    }
}
