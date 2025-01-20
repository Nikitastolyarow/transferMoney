package ru.netology.transferMoney.repository;

import org.springframework.stereotype.Repository;
import ru.netology.transferMoney.model.Card;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CardRepositoryStubImpl implements CardRepository {
    private final Map<String, Card> cards = new HashMap<>();

    public CardRepositoryStubImpl() {

        cards.put("1111222233334444", new Card("1111222233334444", "06/25", "123", 100000));
        cards.put("5555666677778888", new Card("5555666677778888", "11/25", "456", 100));
    }

    @Override
    public Card findCardByNumber(String cardNumber) {
        return cards.get(cardNumber);
    }

    @Override
    public void updateCardBalance(String cardNumber, Integer newBalance) {
        Card card = cards.get(cardNumber);
        if (card != null) {
            card.setBalance(newBalance);
        }
    }
}
