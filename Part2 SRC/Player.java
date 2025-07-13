import java.util.LinkedHashSet;
import java.util.Set;

public class Player {
    private String name;
    private Set<Card> playerCards;
    private int score;

    public Player(String name) {
        this.name = name;
        this.playerCards = new LinkedHashSet<>();
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public Set<Card> getCards() {
        return new LinkedHashSet<>(playerCards);
    }

    public int getScore() {
        calculateScore();
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCards(Set<Card> cards) {
        this.playerCards = new LinkedHashSet<>(cards);
    }

    public void addCard(Card card) {
        playerCards.add(card);
    }

    public void removeCard(Card card) {
        playerCards.remove(card);
    }

    public boolean isEmpty() {
        return playerCards.isEmpty();
    }

    public boolean contains(Card card) {
        return playerCards.contains(card);
    }

    private void calculateScore() {
        for (Card card : playerCards) {
            int value = card.getIntValue();
            switch (value) {
                case 14:
                    score++;
                    break;
                case 13:
                case 12:
                case 11:
                    score += 10;
                    break;
                default:
                    score += value;
                    break;
            }
        }
        playerCards.clear();
    }
}