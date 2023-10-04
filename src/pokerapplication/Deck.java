package pokerapplication;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

	private ArrayList<Card> cards = new ArrayList<Card>();
	int index_1;
	int index_2;
	Random generator = new Random();
	
	public Deck(){
		
		for(int a=0; a<=3; a++){
			for(int b=0; b<=12; b++){
				cards.add(new Card(a,b));
			}
		}
		
		shuffleDeck();
	
	}

	public void shuffleDeck(){
		Collections.shuffle(cards);
	}
	

	public Card drawFromDeck(){
		return cards.remove(cards.size()-1);
	}
	
	public int getTotalCards(){
		return cards.size();
	}
	

	
}
