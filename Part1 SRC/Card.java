import java.util.Objects;

public class Card {
    private CardValue value;

    //constructor
    public Card(CardValue value) {
        this.value = value;
    }

    //getters
    public CardValue getValue() {
        return value;
    }

    public int getIntValue() {
        return value.getCardValue();
    }

    public char getSuit() {
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
}

