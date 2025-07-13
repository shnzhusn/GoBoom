import java.util.ArrayList;
import java.util.Scanner;

public class GoBoom {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Deck deck = new Deck();

        //full deck for checking
        System.out.println("\nDeck: " + deck.getDeck());

        //array for center cards
        ArrayList<Card> center = new ArrayList<>();
        Card leadCard = deck.getCard(0); // lead card removed from the deck
        center.add(leadCard);
        deck.removeCard(0);

        //create instances of players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Player player3 = new Player("Player 3");
        Player player4 = new Player("Player 4");

        //array of players
        Player[] playerList = new Player[4];
        playerList[0] = player1;
        playerList[1] = player2;
        playerList[2] = player3;
        playerList[3] = player4;

        //dealing 7 cards to each player
        for (int i = 0; i < 7; i++) {
            for (Player player : playerList) {
                player.addCard(deck.getCard(0));
                deck.removeCard(0);
            }
        }

        //to determine first player
        int leadCardRank = leadCard.getIntValue();
        int playerTurn = 0;
        switch (leadCardRank) {
            case 14, 5, 9:
                playerTurn = 0;
                break;
            case 2, 6, 10:
                playerTurn = 1;
                break;
            case 3, 7, 11:
                playerTurn = 2;
                break;
            case 4, 8, 12:
                playerTurn = 3;
                break;
        }
        int currentTrick = 1; 

        //main game
        do {
            int trickWinner = playerTurn;
            Card tempLeadCard = leadCard;

            //4 turns for each trick
            for (int i = 0; i < 4; i++) {
                System.out.println("\nTrick #" + currentTrick);
                System.out.println("Player 1: " + player1.getCards());
                System.out.println("Player 2: " + player2.getCards());
                System.out.println("Player 3: " + player3.getCards());
                System.out.println("Player 4: " + player4.getCards());
                System.out.println("Center  : " + center.toString());
                System.out.println("Deck    : " + deck.getDeck());
                System.out.println("Score   : Player 1 = " + player1.getScore() + " | Player 2 = " + player2.getScore()
                        + " | Player 3 = " + player3.getScore() + " | Player 4 = " + player4.getScore());
                System.out.println("Turn    : Player " + (playerTurn + 1));

                // checks input
                do {
                    System.out.print("> ");
                    String userInput = input.nextLine();
                    userInput.toLowerCase();

                    if (userInput.equals("e")) //to end game
                        endGame();
                    else if (userInput.equals("s")) //to restart game
                        startGame();
                    else { //card input
                        try {
                            CardValue playedCardValue = CardValue.fromValue(userInput);
                            Card playedCard = new Card(playedCardValue); //create new card object based on user input

                            //if player does own the card
                            if (playerList[playerTurn].contains(playedCard)) {
                                //if lead card null (first player to play) 
                                if (leadCard == null) {
                                    leadCard = playedCard;
                                    playerList[playerTurn].removeCard(playedCard);
                                    center.add(playedCard);
                                    break;
                                }

                                else {
                                    int playedCardRank = playedCard.getIntValue();
                                    leadCardRank = leadCard.getIntValue();

                                    //same suit card
                                    if (leadCard.getSuit() == playedCard.getSuit()) {
                                        //to check highest rank OR first same suit card for trick #1 (lead card no change)
                                        if (playedCardRank > leadCardRank || leadCard == tempLeadCard) {
                                            leadCard = playedCard;
                                            trickWinner = playerTurn;
                                        }

                                        playerList[playerTurn].removeCard(playedCard);
                                        center.add(playedCard);
                                        break;
                                    }

                                    //same rank card with initial lead card
                                    else if ((center.get(0)).getIntValue() == playedCardRank) {
                                        playerList[playerTurn].removeCard(playedCard);
                                        center.add(playedCard);
                                        break;
                                    }

                                    //other card
                                    else {
                                        throw new IllegalArgumentException("You cannot play card " + playedCard);
                                    }
                                }
                            }
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Input Error: " + ex.getMessage());
                        }
                    }
                } while (true);

                //to cycle through each player
                if (playerTurn == 3)
                  playerTurn = 0;
                else  
                  playerTurn++;
            }

            System.out.println("\n*** Player " + (trickWinner+1) + " wins Trick #" + currentTrick + " ***");
            //for new trick
            currentTrick++;
            center.clear();
            playerTurn = trickWinner;
            leadCard = null;
        } while (true);

    }

    private static void endGame() {
        System.exit(0);
    }

    private static void startGame() {
        main(null);
    }
}
