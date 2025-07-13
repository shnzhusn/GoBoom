import java.util.ArrayList;

public class Player {
    //data fields
    private String name;
    private ArrayList<Card> playerCards = new ArrayList<>();
    private int score;

    //constructor
    public Player(String name) {
        this.name = name;
    }

    //getters
    public ArrayList<Card> getCards() {
        return playerCards;
    }

    public int getScore() {
        return score;
    }

    //methods
    public void addCard(Card card) {
        playerCards.add(card);
    }

    public void removeCard(Card card) {
        playerCards.remove(card);
    }
    
    public boolean contains(Card card) {
        if (playerCards.contains(card))
          return true;
        else
          throw new IllegalArgumentException("You do not have card " + card);
    }
}
