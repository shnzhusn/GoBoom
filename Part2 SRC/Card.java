
import java.util.Objects;

public class Card {
    private CardValue value;

    //constructor
    public Card(CardValue value) {
        this.value = value;
    }

    //getters
    public CardValue getValue() { //both rank and suit
        return value;
    }

    public int getIntValue() { //just rank
        return value.getCardValue();
    }

    public char getSuit() { //just suit
        return value.getSuit();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card other = (Card) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    //other methods
    public static Card fromString(String cardString) {
        if (cardString.equals("null")) {
            return null;
        }
        
        else {
            CardValue value = CardValue.fromValue(cardString);
            Card card = new Card(value);
            return card;
        }
    }
}

