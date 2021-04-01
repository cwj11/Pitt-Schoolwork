
public class Player {

	private MultiDS<Card> faceDownPile;
	private MultiDS<Card> faceUpPile;
	private final static double SHOUT_PROBABILITY = .4;
	private final static double INCORRECT_SHOUT_PROBABILTY = 0.01;
	public boolean foundMatch;
	
	
	public Player() {
		faceDownPile = new MultiDS<Card>(52);
		faceUpPile = new MultiDS<Card>(52);
		foundMatch = false;
	}
	
	public boolean addCard(Card c) {
		return faceDownPile.addItem(c);
	}
	
	public Card getFaceUpCard() {
		return faceUpPile.bottom();
	}
	
	public Card removeFaceUpCard() {
		return faceUpPile.removeItem();
	}
	
	public boolean flipCard() {
		return faceUpPile.addItem(faceDownPile.removeItem());
	}
	
	public String toString() {
		return "Contents: \n" + faceDownPile.toString();
	}
	
	public Card getTopCard() {
		return faceUpPile.top();
	}
	
	public int numCards() {
		return faceDownPile.size() + faceUpPile.size();
	}
	
	public int getFaceUpPileSize() {
		return faceUpPile.size();
	}
	
	public boolean faceUpPileEmpty() {
		return faceUpPile.empty();
	}
	
	public void checkFaceDownPile() {
		if(faceDownPile.empty()) {
			while(!faceUpPile.empty()) {
				faceDownPile.addItem(faceUpPile.removeItem());
			}
		}
			
	}
	
	public void addCards(MultiDS<Card> cards) {
		cards.shuffle();
		for(int i = 0; i < cards.size(); i++) {
			addCard(cards.removeItem());
		}
	}
	
	public MultiDS<Card> getfaceUpPile() {
		MultiDS<Card> pile = faceUpPile;
		faceUpPile.clear();
		return pile;
	}
	
	public boolean shout(boolean shouldShout) {
		if(shouldShout) {
			if(Math.random() < SHOUT_PROBABILITY)
				return true;
		} else
			if(Math.random() < INCORRECT_SHOUT_PROBABILTY)
				return true;
		return false;
	}

}