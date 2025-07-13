import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    //data field
    private ArrayList<Card> deck = new ArrayList<>();

    //constructor
    public Deck(){
        this.deck = new ArrayList<>();
        
        for (CardValue value: CardValue.values()) {
            Card card = new Card(value);
            deck.add(card);
        }
        Collections.shuffle(deck);
    }

    //getters
    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Card getCard(int index) {
        return deck.get(index);
    }

    //methods
    public void removeCard(int index) {
        deck.remove(index);
    }
}
