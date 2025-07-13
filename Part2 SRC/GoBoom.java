import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GoBoom {
    //data fields
    Player[] playerList = new Player[4];
    private int currentRound;
    private int currentTrick;
    private int playerTurn;
    private int tempPlayerTurn;
    private Card tempLeadCard;
    private Card leadCard;
    Deck deck;
    ArrayList<Card> center = new ArrayList<>();
    Map<Player, Integer> scores = new HashMap<>();

    public void playGame() {
        Scanner input = new Scanner(System.in);

        //create instances of players
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Player player3 = new Player("Player 3");
        Player player4 = new Player("Player 4");

        //array of players
        playerList[0] = player1;
        playerList[1] = player2;
        playerList[2] = player3;
        playerList[3] = player4;

        //initialize score
        for (Player player: playerList)
            scores.put(player, 0);
        
        //main game
        //loop for round
        currentRound = 1;
        do {
            currentTrick = 1;
            boolean goBoom = false; 

            System.out.println("\n*** ROUND " + currentRound + " ***"); 

            //full deck for checking
            deck = new Deck();
            System.out.println("Deck: " + deck.getDeck());

            //array for center cards
            leadCard = deck.getCard(0); // lead card removed from the deck
            center.clear();
            center.add(leadCard);
            deck.removeCard(0);

            //dealing 7 cards to each player
            for (int i = 0; i < 7; i++) {
                for (Player player : playerList) {
                    player.addCard(deck.getCard(0));
                    deck.removeCard(0);
                }
            }

            //to determine first player
            int leadCardRank = leadCard.getIntValue();
            playerTurn = 0;
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

            //loop for trick
            do {
                int trickWinner = playerTurn;
                tempLeadCard = leadCard;
                tempPlayerTurn = playerTurn;

                //4 turns for each trick
                for (int i = 0; i < 4; i++) {
                    System.out.println("\nTrick #" + currentTrick);
                    System.out.println("Player 1: " + player1.getCards());
                    System.out.println("Player 2: " + player2.getCards());
                    System.out.println("Player 3: " + player3.getCards());
                    System.out.println("Player 4: " + player4.getCards());
                    System.out.println("Center  : " + center.toString());
                    System.out.println("Deck    : " + deck.getDeck());
                    System.out.println("Turn    : " + playerList[playerTurn].getName());

                    // checks input
                    do {
                        System.out.print("> ");
                        String userInput = input.nextLine();
                        userInput.toLowerCase();

                        if (userInput.equals("e")) //to end game
                            endGame();
                        else if (userInput.equals("r")) //to restart game
                            playGame();
                        else if (userInput.equals("s")) { //save game
                            System.out.print("> Enter filename: ");
                            String filename = input.nextLine();
                            tempPlayerTurn = i;
                            saveGame(filename);
                        }
                        else if (userInput.equals("l")) { //to load saved game
                            System.out.print("> Enter filename: ");
                            String filename = input.nextLine();
                            loadGame(filename);
                            i = tempPlayerTurn-1;
                            playerTurn--;
                            break;
                        }

                        else if (userInput.equals("d")) { //draw card from deck
                           if (deck.isEmpty()) {
                                i++;
                                playerTurn = playerTurn + 1;
                            }
                            drawCard();
                            i--;
                            playerTurn--;
                            break;
                        }

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
                    changeTurn();

                }

                System.out.println("\n*** Player " + (trickWinner+1) + " wins Trick #" + currentTrick + " ***");

                //for new trick
                currentTrick++;
                center.clear();
                playerTurn = trickWinner;
                leadCard = null;

                //check if any player has won
                for (Player player : playerList) {
                    if(player.isEmpty()) {
                        goBoom = true;
                        for(int i = 0; i < 4; i++) {
                            int temp = scores.get(playerList[i]);
                            scores.put(playerList[i], temp+(playerList[i].getScore()));
                        }
                        System.out.println("*** END OF ROUND " + currentRound + " ***");
                        System.out.println("Score   : Player 1 = " + scores.get(player1) + " | Player 2 = " + scores.get(player2)
                            + " | Player 3 = " + scores.get(player3) + " | Player 4 = " + scores.get(player4));
                        currentRound++;
                        break;
                    }
                }
            } while (!goBoom);
        } while (true);
    }

    public void changeTurn() {
        if (playerTurn == 3)
          playerTurn = 0;
        else  
          playerTurn++;
    }

    public void saveGame(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println(currentRound);
            writer.println(currentTrick);

            // save player cards
            for (Player player : playerList) {
                Set<Card> cards = player.getCards();
                writer.print("[");
                for (Card card : cards) {
                    writer.print(card.toString() + ", ");
                }
                writer.println("]");
                writer.println(scores.get(player));
            }

            // save center cards
            writer.print("[");
            for (Card card : center) {
                writer.print(card.toString() + ", ");
            }
            writer.println("]");

            writer.println(leadCard);
            writer.println(tempLeadCard);

            writer.println(deck.getDeck());
            writer.println(playerTurn);
            writer.println(tempPlayerTurn);

            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the game: " + e.getMessage());
        }
    }

    public void loadGame(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            currentRound = Integer.parseInt(scanner.nextLine());
            currentTrick = Integer.parseInt(scanner.nextLine());

            // load player cards
            for (int i = 0; i < 4; i++) {
                String line = scanner.nextLine();
                String[] cardStrings = line.substring(1, line.length() - 1).split(", ");
                Set<Card> cards = new LinkedHashSet<>();
                for (String cardString : cardStrings) {
                    if (cardString.isBlank())
                      continue;
                    Card card = Card.fromString(cardString);
                    cards.add(card);
                }
                playerList[i].setCards(cards);
                scores.put(playerList[i], Integer.parseInt(scanner.nextLine()));
                //playerList[i].setScore(Integer.parseInt(scanner.nextLine()));
            }

            // load center cards
            String line = scanner.nextLine();
            String[] cardStrings = line.substring(1, line.length() - 1).split(", ");
            center.clear();
            for (String cardString : cardStrings) {
                if (cardString.isBlank())
                  continue;
                CardValue cardValue = CardValue.fromValue(cardString);
                Card card = new Card(cardValue);
                center.add(card);
            }
            
            leadCard = Card.fromString(scanner.nextLine());
            tempLeadCard = Card.fromString(scanner.nextLine());
            deck = Deck.fromString(scanner.nextLine());
            playerTurn = Integer.parseInt(scanner.nextLine());
            tempPlayerTurn = Integer.parseInt(scanner.nextLine());

            System.out.println("Game loaded successfully!");
            System.out.println("\nCurrent Round: " + currentRound);
            System.out.println("Current Trick: " + currentTrick);
            System.out.println(playerList[playerTurn].getName() + "'s turn");
        } catch (IOException e) {
            System.out.println("An error occurred while loading the game: " + e.getMessage());
        }
    }

    public void endGame() {
        System.exit(0);
    }

    public void drawCard() {
        if (deck.isEmpty()) //if deck is empty skip player
            System.out.println("Player " + (playerTurn+1)  + " skipped turn");
        else {
            playerList[playerTurn].addCard(deck.getCard(0));
            deck.removeCard(0);
        }
    }

    public static void main(String[] args) {
        GoBoom game = new GoBoom();
        game.playGame();
    }

    
}
