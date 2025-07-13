import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    // Data field
    private ArrayList<Card> deck;

    // Constructor
    public Deck() {
        deck = new ArrayList<>();
        for (CardValue value : CardValue.values()) {
            Card card = new Card(value);
            deck.add(card);
        }
        shuffle();
    }

    // Getters
    public ArrayList<Card> getDeck() {
        return new ArrayList<>(deck);
    }

    public Card getCard(int index) {
        return deck.get(index);
    }

    // Methods
    public void removeCard(int index) {
        deck.remove(index);
    }

    public void removeCard(Card card) {
        deck.remove(card);
    }

    public void clearDeck() {
        deck.clear();
    }
    
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public void addCard(Card card) {
        deck.add(card);
    }

    

    public static Deck fromString(String deckString) {
        Deck deck = new Deck();
        deck.clearDeck();
        String[] cardValues = deckString.substring(1, deckString.length() - 1).split(", ");
        for (String cardValue : cardValues) {
            try {
                CardValue value = CardValue.fromValue(cardValue);
                Card card = new Card(value);
                deck.addCard(card);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid card value: " + cardValue);
            }
        }
        return deck;
    }
}