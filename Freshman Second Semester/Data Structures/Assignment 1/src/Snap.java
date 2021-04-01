import java.util.ArrayList;
import java.util.Scanner;

public class Snap {

	private static MultiDS<Card> middlePile;
	private static Player[] players;
	private static int numPlayers;
	private static int numRounds;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to the game of Snap!");
		System.out.print("Enter the number of rounds: ");
		numRounds = sc.nextInt();
		System.out.print("Enter the number of players: ");
		numPlayers = sc.nextInt();
		players = new Player[numPlayers];
		middlePile = new MultiDS<Card>(52);
		for(int i = 0; i < numPlayers; i++) {
			players[i] = new Player();
		}	for(Card.Suits s: Card.Suits.values()) {
			for(Card.Ranks r: Card.Ranks.values()) {
				middlePile.addItem(new Card(s,r));
			}
		}

		playGame();


	}

	private static void playGame() {
		System.out.println("Now dealing the cards to the players...");
		dealCards();
		printFaceDownPile();
		printPool();
		System.out.println("Starting the game!");
		int roundNumber = 0;
		while(allPlayersHaveCards() && roundNumber < numRounds) {
			System.out.print("Rnd #" + roundNumber + ": ");
			flipCards();
			ArrayList<ArrayList<Integer>> matches = new ArrayList<ArrayList<Integer>>();
			matches.add(new ArrayList<Integer>());
			int numMatches = 0;
			int snapPoolMatch = -1;
			for(int i = 0; i < numPlayers-1; i++) {
				if(!players[i].foundMatch) {
					for(int j = i + 1; j < numPlayers; j++) {
						if(!players[j].foundMatch) {
							if(players[i].getFaceUpCard().compareTo(players[j].getFaceUpCard()) == 0) {
								if(!matches.get(numMatches).contains(i)) {
									players[i].foundMatch = true;
									matches.get(numMatches).add(i);
								}
								matches.get(numMatches).add(j);
								players[j].foundMatch = true;
							}
						}
					}
					if(middlePile.size() != 0 && middlePile.bottom().compareTo(players[i].getFaceUpCard()) == 0) {
						snapPoolMatch = i;
						System.out.println(i);
					}
					if(matches.get(numMatches).size() > 0) {
						numMatches++;
						matches.add(new ArrayList<Integer>());
					}
				}
			}
			for(Player p: players) {
				p.foundMatch = false;
			}
			roundNumber++;
			int numShouts = 0;
			int playerShouts = -1;
			if(numMatches > 0 || snapPoolMatch > -1) {
				for(int i = 0; i < numPlayers; i++) {
					if(players[i].shout(true)) {
						playerShouts = i;
						numShouts++;
					}
				}
				if(numShouts > 1) {
					System.out.println("Match but no winner! " + numShouts + " players shouted!");
				}else if(numShouts == 1) {
					for(int i = 0; i < matches.size(); i++) {
						if(matches.get(i).size() != 0) {
							System.out.println("Match! Player " + playerShouts + " shouted alone.");
							switch(matches.get(i).size()) {
							case 2: System.out.print(players[matches.get(i).get(0)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(0)
									+ " (" + players[matches.get(i).get(0)].getFaceUpPileSize() + " cards) matches "
											+ players[matches.get(i).get(1)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(1)
													+ " (" + players[matches.get(i).get(1)].getFaceUpPileSize() + " cards)"); break;
							case 3: System.out.print(players[matches.get(i).get(0)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(0)
									+ " (" + players[matches.get(i).get(0)].getFaceUpPileSize() + " cards) matches "
											+ players[matches.get(i).get(1)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(1)
													+ " (" + players[matches.get(i).get(1)].getFaceUpPileSize() + " cards)" + " and "
															+ players[matches.get(i).get(2)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(2)
																		+ " (" + players[matches.get(i).get(2)].getFaceUpPileSize() + " cards)"); break;
							default: System.out.print(players[matches.get(i).get(0)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(0)
											+ " (" + players[matches.get(i).get(0)].getFaceUpPileSize() + " cards) matches "
													+ players[matches.get(i).get(1)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(1)
																+ " (" + players[matches.get(i).get(1)].getFaceUpPileSize() + " cards), "
																		+ players[matches.get(i).get(2)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(2)
																				+ " (" + players[matches.get(i).get(2)].getFaceUpPileSize() + " cards), and "
																						+ players[matches.get(i).get(3)].getFaceUpCard().toString() + " of Player " + matches.get(i).get(3)
																								+ " (" + players[matches.get(i).get(3)].getFaceUpPileSize() + " cards)"); break;
							}
							
							for(int j = 0; j < matches.get(i).size(); i++) {
								if(playerShouts != matches.get(i).get(j)) {
									players[playerShouts].addCards(players[matches.get(i).get(j)].getfaceUpPile());
								}
							}
						}
						
					}
					if(snapPoolMatch > -1) {
						if(numMatches > 0)
							System.out.print(" and " + middlePile.bottom().toString() + " of the Snap Pool.");
						else
							System.out.print(players[snapPoolMatch].getFaceUpCard().toString() + " of Player " + snapPoolMatch
									+ " (" + players[snapPoolMatch].getFaceUpPileSize() + " cards) matches " + middlePile.bottom().toString() + " of the Snap Pool.");
					}
					System.out.println();
					if(snapPoolMatch > -1) {
						players[playerShouts].addCards(middlePile);
						middlePile.clear();
						snapPoolMatch = -1;
					}
				}else {
					System.out.println("Match but no one shouted!");
				}
			}else {
				System.out.println("No Match!");
				for(int i = 0; i < numPlayers; i++) {
					if(players[i].shout(false)) {
						System.out.println("Player " + i + " shouted incorrectly!");
						System.out.println(players[i].getFaceUpPileSize() + " cards moved to the snap pool.");
						while(players[i].faceUpPileEmpty()) {
							middlePile.addItem(players[i].getFaceUpCard());
						}
					}
				}
			}
			checkAllFaceDownPiles();
		}
		for(int i = 0; i < players.length; i++) {
			if(players[i].numCards() == 0) {
				System.out.println("Player " + i + " ran out of cards!");
			}
		}
		printGameResult();


	}


	private static void dealCards() {
		middlePile.shuffle();
		int numDeals = 52/numPlayers;
		for(int i = 0; i < numDeals; i++) {
			for(int j = 0; j < numPlayers; j++) {
				players[j].addCard(middlePile.removeItem());
			}
		}
	}


	public static void printFaceDownPile() {
		for(int i = 0; i < numPlayers; i++) {
			System.out.println("Here is Player " + i + "'s face-down pile:\n" + players[i].toString());
			System.out.println();
		}
	}

	public static void printPool() {
		System.out.println("Here is the Snap Pool:\nContents:\n" + middlePile.toString());
		System.out.println();
	}

	public static void flipCards() {
		for(Player p: players) {
			p.flipCard();
		}
	}

	public static boolean allPlayersHaveCards() {
		for(int i = 0; i < numPlayers; i++) {
			if(players[i].numCards() == 0)
				return false;
		}
		return true;
	}
	
	public static void checkAllFaceDownPiles() {
		for(Player p: players) {
			p.checkFaceDownPile();
		}
	}
	
	public static void printGameResult() {
		for(int i = 0; i < players.length; i++) {
			System.out.println("Player " + i + " has " + players[i].numCards() + " cards.");
		}
	}
	

}
