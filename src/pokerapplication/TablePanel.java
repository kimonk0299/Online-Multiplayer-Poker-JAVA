package pokerapplication;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;



class TablePanel extends JPanel implements PokerConstant{
	
	String relativePath = System.getProperty("user.dir") + "\\pokerapplication\\src\\images\\";
	
	

	Card p1Card1, p1Card2, p2Card1, p2Card2, flop1, flop2, flop3, flop4, flop5 ;
	
	private PokerClient parent;
	
	private int currentPlayerIs;
	private String playerName;
	private String playerName2;
	
	private double p1TotalChips;
	private double p2TotalChips;
	double currentPot;
	
	private double p1TotalChipsBid;
	private double p2TotalChipsBid;
	private double currentBidToMatch;
	
	private int playerToken;
	
	private int currentGameRound;
	
	private String bubbleStatus = "default";
	
	private String winnerIs = "winner is ..... ";
	private int p1ValueInInt;
	private int P2ValueInInt;
	private Card p1WonWithHighCardOf;
	private Card p2WonWithHighCardOf;
	

	public TablePanel( PokerClient gui){
		//card = selectCard; Card selectCard,
		parent = gui;
	}
	
	

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.setColor(Color.white);
	
		ImageIcon bg;
		try {
			//background table
			bg = new ImageIcon(ImageIO.read(new File(relativePath + "pokerTableBlack.png")));
			//System.out.println(System.getProperty("user.dir"));
			bg.paintIcon(this, g, 0, 0);
			
			switch(currentGameRound){
			case WAITINGROUND:
					if(playerName != null && currentPlayerIs == PLAYER1){
						//player details
						drawPlayerPicture(g, 30, 70);
						drawPlayerDetailBox(g, 5, 158);
						g.drawString(playerName, 60, 180);
						g.drawString("�" + Double.toString(p1TotalChips), 60, 200);
						g.drawString("�" + Double.toString(currentPot), 450, 190);
						drawNotification(g, "waitingForPlayers.png", 0, 0);
						g.drawString("�" + Double.toString(getP1TotalChipsBid()), 400, 180);
						g.drawString("�" + Double.toString(getP2TotalChipsBid()), 400, 200);
					}else if(playerName2 != null && currentPlayerIs == PLAYER2){
						//player details
						drawPlayerPicture(g, 30, 270);
						drawPlayerDetailBox(g, 5, 358);
						g.drawString(playerName2, 60, 380);
						g.drawString("�" + Double.toString(p2TotalChips), 60, 400);
						g.drawString("�" + Double.toString(currentPot), 400, 150);
					}
					break;
			case PREFLOPROUND:
					//p1 id holder
					drawPlayerPicture(g, 30, 70);
					drawPlayerDetailBox(g, 5, 158);
					g.drawString(playerName, 60, 180);
					g.drawString("�" + Double.toString(p1TotalChips), 60, 200);
					
					//p2 id holder
					drawPlayerPicture(g, 30, 270);
					drawPlayerDetailBox(g, 5, 358);
					g.drawString(playerName2, 60, 380);
					g.drawString("�" + Double.toString(p2TotalChips), 60, 400);
					//notification
					drawNotification(g, "preFlopRound.png", 0, 0);
					drawBubbleStatus(g, 370, 0);
					g.drawString(getBubbleStatus(), 385, 30);
					if(playerName != null && currentPlayerIs == PLAYER1){
						//p1 cards
						drawCard(relativePath, p1Card1, g, 90, 100);
						drawCard(relativePath, p1Card2, g, 110, 100);
						//p2 facedown
						drawFaceDown( g, 90, 300);
						drawFaceDown( g, 110, 300);						
					}else if(playerName2 != null && currentPlayerIs == PLAYER2){
						//p2 cards
						drawCard(relativePath, p2Card1, g, 90, 300);
						drawCard(relativePath, p2Card2, g, 110, 300);
						//p1 facedown
						drawFaceDown( g, 90, 100);
						drawFaceDown( g, 110, 100);	
					}					
					g.drawString("�" + Double.toString(getP1TotalChipsBid()), 400, 180);
					g.drawString("�" + Double.toString(getP2TotalChipsBid()), 400, 200);
					g.drawString("�" + Double.toString(currentPot), 450, 190);
					break;
			case FLOPROUND:
					//p1 id holder
					drawPlayerPicture(g, 30, 70);
					drawPlayerDetailBox(g, 5, 158);
					g.drawString(playerName, 60, 180);
					g.drawString("�" + Double.toString(p1TotalChips), 60, 200);
					//p2 id holder
					drawPlayerPicture(g, 30, 270);
					drawPlayerDetailBox(g, 5, 358);
					g.drawString(playerName2, 60, 380);
					g.drawString("�" + Double.toString(p2TotalChips), 60, 400);
					//table values
					g.drawString("�" + Double.toString(getP1TotalChipsBid()), 400, 180);
					g.drawString("�" + Double.toString(getP2TotalChipsBid()), 400, 200);
					g.drawString("�" + Double.toString(currentPot), 450, 190);
					drawNotification(g, "flopRound.png", 0, 0);
					drawBubbleStatus(g, 370, 0);
					g.drawString(getBubbleStatus(), 385, 30);
				    if(playerName != null && currentPlayerIs == PLAYER1){
				    	//p1 hand card
						drawCard(relativePath, p1Card1, g, 90, 100);
						drawCard(relativePath, p1Card2, g, 110, 100);
						//flopCards
						drawCard(relativePath, flop1, g, 220, 190);
						drawCard(relativePath, flop2, g, 270, 190);
						drawCard(relativePath, flop3, g, 320, 190);					
						//draw p2 facedown cards
						drawFaceDown( g, 90, 300);
						drawFaceDown( g, 110, 300);
				    }else if(playerName2 != null && currentPlayerIs == PLAYER2){
						//p1 facedown cards
						drawFaceDown( g, 90, 100);
						drawFaceDown( g, 110, 100);	
						//flopCards
						drawCard(relativePath, flop1, g, 220, 190);
						drawCard(relativePath, flop2, g, 270, 190);
						drawCard(relativePath, flop3, g, 320, 190);
						//draw p2 hand cards stuffs	
						drawCard(relativePath, p2Card1, g, 90, 300);
						drawCard(relativePath, p2Card2, g, 110, 300);
				    }
				    break;
			case TURNROUND:
				//p1 id holder
					drawPlayerPicture(g, 30, 70);
					drawPlayerDetailBox(g, 5, 158);
					g.drawString(playerName, 60, 180);
					g.drawString("�" + Double.toString(p1TotalChips), 60, 200);
					//p2 id holder
					drawPlayerPicture(g, 30, 270);
					drawPlayerDetailBox(g, 5, 358);
					g.drawString(playerName2, 60, 380);
					g.drawString("�" + Double.toString(p2TotalChips), 60, 400);
					//table values
					g.drawString("�" + Double.toString(getP1TotalChipsBid()), 400, 180);
					g.drawString("�" + Double.toString(getP2TotalChipsBid()), 400, 200);
					g.drawString("�" + Double.toString(currentPot), 450, 190);
					drawNotification(g, "turnRound.png", 0, 0);
					drawBubbleStatus(g, 370, 0);
					g.drawString(getBubbleStatus(), 385, 30);
					if(playerName != null && currentPlayerIs == PLAYER1){
				    	//p1 hand card
						drawCard(relativePath, p1Card1, g, 90, 100);
						drawCard(relativePath, p1Card2, g, 110, 100);
						//flopCards
						drawCard(relativePath, flop1, g, 220, 190);
						drawCard(relativePath, flop2, g, 270, 190);
						drawCard(relativePath, flop3, g, 320, 190);
						drawCard(relativePath, flop4, g, 370, 190);
						//draw p2 facedown cards
						drawFaceDown( g, 90, 300);
						drawFaceDown( g, 110, 300);
				    }else if(playerName2 != null && currentPlayerIs == PLAYER2){
						//p1 facedown cards
						drawFaceDown( g, 90, 100);
						drawFaceDown( g, 110, 100);	
						//flopCards
						drawCard(relativePath, flop1, g, 220, 190);
						drawCard(relativePath, flop2, g, 270, 190);
						drawCard(relativePath, flop3, g, 320, 190);
						drawCard(relativePath, flop4, g, 370, 190);
						//draw p2 hand cards stuffs	
						drawCard(relativePath, p2Card1, g, 90, 300);
						drawCard(relativePath, p2Card2, g, 110, 300);
				    }
					break;
					
			   case RIVERROUND:
				    //p1 id holder
					drawPlayerPicture(g, 30, 70);
					drawPlayerDetailBox(g, 5, 158);
					g.drawString(playerName, 60, 180);
					g.drawString("�" + Double.toString(p1TotalChips), 60, 200);
					//p2 id holder
					drawPlayerPicture(g, 30, 270);
					drawPlayerDetailBox(g, 5, 358);
					g.drawString(playerName2, 60, 380);
					g.drawString("�" + Double.toString(p2TotalChips), 60, 400);
					//table values
					g.drawString("�" + Double.toString(getP1TotalChipsBid()), 400, 180);
					g.drawString("�" + Double.toString(getP2TotalChipsBid()), 400, 200);
					g.drawString("�" + Double.toString(currentPot), 450, 190);
					drawNotification(g, "riverRound.png", 0, 0);
					drawBubbleStatus(g, 370, 0);
					g.drawString(getBubbleStatus(), 385, 30);
					if(playerName != null && currentPlayerIs == PLAYER1){
				    	//p1 hand card
						drawCard(relativePath, p1Card1, g, 90, 100);
						drawCard(relativePath, p1Card2, g, 110, 100);
						//flopCards
						drawCard(relativePath, flop1, g, 220, 190);
						drawCard(relativePath, flop2, g, 270, 190);
						drawCard(relativePath, flop3, g, 320, 190);
						drawCard(relativePath, flop4, g, 370, 190);
						drawCard(relativePath, flop5, g, 420, 190);
						//draw p2 facedown cards
						drawFaceDown( g, 90, 300);
						drawFaceDown( g, 110, 300);
				    }else if(playerName2 != null && currentPlayerIs == PLAYER2){
						//p1 facedown cards
						drawFaceDown( g, 90, 100);
						drawFaceDown( g, 110, 100);	
						//flopCards
						drawCard(relativePath, flop1, g, 220, 190);
						drawCard(relativePath, flop2, g, 270, 190);
						drawCard(relativePath, flop3, g, 320, 190);
						drawCard(relativePath, flop4, g, 370, 190);
						drawCard(relativePath, flop5, g, 420, 190);
						//draw p2 hand cards stuffs	
						drawCard(relativePath, p2Card1, g, 90, 300);
						drawCard(relativePath, p2Card2, g, 110, 300);
				    }
					break;
					
			   case FINDWINNERROUND:
				 //p1 id holder
					drawPlayerPicture(g, 30, 70);
					drawPlayerDetailBox(g, 5, 158);
					g.drawString(playerName, 60, 180);
					g.drawString("�" + Double.toString(p1TotalChips), 60, 200);
					//p2 id holder
					drawPlayerPicture(g, 30, 270);
					drawPlayerDetailBox(g, 5, 358);
					g.drawString(playerName2, 60, 380);
					g.drawString("�" + Double.toString(p2TotalChips), 60, 400);
					//table values
					g.drawString("�" + Double.toString(getP1TotalChipsBid()), 400, 180);
					g.drawString("�" + Double.toString(getP2TotalChipsBid()), 400, 200);
					g.drawString("�" + Double.toString(currentPot), 450, 190);
					drawNotification(g, "winnerBanner.png", 200, 270);
					g.drawString(getWinnerIs(), 210, 285);
					drawBubbleStatus(g, 370, 0);
					g.drawString(getBubbleStatus(), 385, 30);
					if(playerName != null && currentPlayerIs == PLAYER1){
					//p1 hand card
					drawCard(relativePath, p1Card1, g, 90, 100);
					drawCard(relativePath, p1Card2, g, 110, 100);
					drawCard(relativePath, p2Card1, g, 90, 300);
					drawCard(relativePath, p2Card2, g, 110, 300);
					
					}else if(playerName2 != null && currentPlayerIs == PLAYER2){
					//draw p2 hand cards stuffs	
					drawCard(relativePath, p1Card1, g, 90, 100);
					drawCard(relativePath, p1Card2, g, 110, 100);
					drawCard(relativePath, p2Card1, g, 90, 300);
					drawCard(relativePath, p2Card2, g, 110, 300);
					}
					//flopCards
					drawCard(relativePath, flop1, g, 220, 190);
					drawCard(relativePath, flop2, g, 270, 190);
					drawCard(relativePath, flop3, g, 320, 190);
					drawCard(relativePath, flop4, g, 370, 190);
					drawCard(relativePath, flop5, g, 420, 190);
			   break;
			   
			  case FOLDROUND:
				  	//p1 id holder
					drawPlayerPicture(g, 30, 70);
					drawPlayerDetailBox(g, 5, 158);
					g.drawString(playerName, 60, 180);
					g.drawString("�" + Double.toString(p1TotalChips), 60, 200);
					//p2 id holder
					drawPlayerPicture(g, 30, 270);
					drawPlayerDetailBox(g, 5, 358);
					g.drawString(playerName2, 60, 380);
					g.drawString("�" + Double.toString(p2TotalChips), 60, 400);
					//table values
					g.drawString("�" + Double.toString(getP1TotalChipsBid()), 400, 180);
					g.drawString("�" + Double.toString(getP2TotalChipsBid()), 400, 200);
					g.drawString("�" + Double.toString(currentPot), 450, 190);
					
					drawBubbleStatus(g, 370, 0);
					g.drawString(getBubbleStatus(), 385, 30);
					drawNotification(g, "winnerBanner.png", 200, 270);
					g.drawString(getWinnerIs(), 210, 285);
					if(playerName != null && currentPlayerIs == PLAYER1){
				    	//p1 hand card
						drawCard(relativePath, p1Card1, g, 90, 100);
						drawCard(relativePath, p1Card2, g, 110, 100);
						//flopCards
						drawCard(relativePath, flop1, g, 220, 190);
						drawCard(relativePath, flop2, g, 270, 190);
						drawCard(relativePath, flop3, g, 320, 190);
						drawCard(relativePath, flop4, g, 370, 190);
						drawCard(relativePath, flop5, g, 420, 190);
						//draw p2 facedown cards
						drawFaceDown( g, 90, 300);
						drawFaceDown( g, 110, 300);
				    }else if(playerName2 != null && currentPlayerIs == PLAYER2){
						//p1 facedown cards
						drawFaceDown( g, 90, 100);
						drawFaceDown( g, 110, 100);	
						//flopCards
						drawCard(relativePath, flop1, g, 220, 190);
						drawCard(relativePath, flop2, g, 270, 190);
						drawCard(relativePath, flop3, g, 320, 190);
						drawCard(relativePath, flop4, g, 370, 190);
						drawCard(relativePath, flop5, g, 420, 190);
						//draw p2 hand cards stuffs	
						drawCard(relativePath, p2Card1, g, 90, 300);
						drawCard(relativePath, p2Card2, g, 110, 300);
				    }
			  break;
			   	
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}
	

	 private void drawCard(String path, Card card, Graphics g, int x, int y) throws IOException{
		 ImageIcon img;
			 img = new ImageIcon(ImageIO.read(new File(path+card.toString()+".png")));
			 Image card1 = img.getImage();
			 Image card1New = card1.getScaledInstance(45, 65, java.awt.Image.SCALE_SMOOTH);
			 ImageIcon card1Icon = new ImageIcon(card1New);
			 card1Icon.paintIcon(this, g, x, y);
	 }
	 

	 private void drawFaceDown(Graphics g, int x, int y) throws IOException{
		ImageIcon img;
		img = new ImageIcon(ImageIO.read(new File(relativePath + "faceDown.png")));
		Image card1 = img.getImage();
		Image card1New = card1.getScaledInstance(45, 65, java.awt.Image.SCALE_SMOOTH);
		ImageIcon card1Icon = new ImageIcon(card1New);
		card1Icon.paintIcon(this, g, x, y);
	 }
	 

	 private void drawPlayerDetailBox(Graphics g, int x, int y) throws IOException{
		 ImageIcon bg;
		 bg = new ImageIcon(ImageIO.read(new File(relativePath + "playerDetailBox.png")));
		 bg.paintIcon(this, g, x, y);
	 }
	 

	 private void drawPlayerPicture(Graphics g, int x, int y) throws IOException{
		 ImageIcon bg;
		 bg = new ImageIcon(ImageIO.read(new File(relativePath + "playerPic.png")));
		 bg.paintIcon(this, g, x, y);
	 }
	 

	 private void drawBubbleStatus(Graphics g, int x, int y) throws IOException{
		 ImageIcon bg;
		 bg = new ImageIcon(ImageIO.read(new File(relativePath + "bubbleStatus.png")));
		 bg.paintIcon(this, g, x, y);
	 }
	 

	 private void drawNotification(Graphics g, String fileName, int x, int y) throws IOException{
		 ImageIcon bg;
		 bg = new ImageIcon(ImageIO.read(new File(relativePath + fileName)));
		 bg.paintIcon(this, g, x, y);
	 }
	 
	 //Accessors and Mutators
	 

	 public void setCurrentPlayerIs(int currentPlayerInt){
		currentPlayerIs = currentPlayerInt;
		repaint();
	}
	

	public void setCurrentGameRound(int round){
		currentGameRound = round;
		repaint();
	}
	

	public int getCurrentGameRound(){
		return currentGameRound;
	}
	

	public double getCurrentPot(){
		return currentPot;
	}
	

	public void setCurrentPot(double currentPot){
		this.currentPot = currentPot;
		repaint();
	}
	

	public void setP1TotalChips(double p1TotalChips){
		this.p1TotalChips = p1TotalChips;
		repaint();
	}
	

	public double getP1TotalChips(){
		return p1TotalChips;
	}
		

	public void setP2TotalChips(double p2TotalChips){
		this.p2TotalChips = p2TotalChips;
		repaint();
	}
	

	public double getP2TotalChips(){
		return p2TotalChips;
	}
	

	public void setPlayerName(String name){
		playerName = name;
		repaint();
	}
		

	public String getPlayerName(){
		return playerName;
	}
	

	public void setPlayerName2(String name){
		playerName2 = name;
		repaint();
	}
	

	public String getPlayerName2(){
		return playerName2;
	}
	

	public void setP1Card1(Card p1Card1){
		this.p1Card1 = p1Card1;
		repaint();
	}
	

	public void setP1Card2(Card p1Card2){
		this.p1Card2 = p1Card2;
		repaint();
	}
	

	public void setP2Card1(Card p2Card1){
		this.p2Card1 = p2Card1;
		repaint();
	}
	

	public void setP2Card2(Card p2Card2){
		this.p2Card2 = p2Card2;
		repaint();
	}
	

	public void setFlop1(Card flop1){
		this.flop1 = flop1;
		repaint();
	}
		

	public void setFlop2(Card flop2){
		this.flop2 = flop2;
		repaint();
	}
	

	public void setFlop3(Card flop3){
		this.flop3 = flop3;
		repaint();
	}
	

	public void setFlop4(Card flop4){
		this.flop4 = flop4;
		repaint();
	}
	

	public void setFlop5(Card flop5){
		this.flop5 = flop5;
		repaint();
	}  	 
	

	public double getP1TotalChipsBid() {
		return p1TotalChipsBid;
	}
	

	public void setP1TotalChipsBid(double p1TotalChipsBid) {
		this.p1TotalChipsBid = p1TotalChipsBid;
		repaint();
	}


	public double getP2TotalChipsBid() {
		return p2TotalChipsBid;
	}
	

	public void setP2TotalChipsBid(double p2TotalChipsBid) {
		this.p2TotalChipsBid = p2TotalChipsBid;
		repaint();
	}
	

	public double getCurrentBidToMatch() {
		return currentBidToMatch;
	}

	public void setCurrentBidToMatch(double currentBidToMatch) {
		this.currentBidToMatch = currentBidToMatch;
		repaint();
	}
	

	public int getPlayerToken() {
		return playerToken;
	}
	

	public void setPlayerToken(int playerToken) {
		this.playerToken = playerToken;
		repaint();
	}
	

	public String getBubbleStatus() {
		return bubbleStatus;
	}
	

	public void setBubbleStatus(String bubbleStatus) {
		this.bubbleStatus = bubbleStatus;
		repaint();
	}
	

	public String getWinnerIs() {
		return winnerIs;
	}
	

	public void setWinnerIs(String winnerIs) {
		this.winnerIs = winnerIs;
		repaint();
	}

	public int getP1ValueInInt() {
		return p1ValueInInt;
	}
	

	public void setP1ValueInInt(int p1ValueInInt) {
		this.p1ValueInInt = p1ValueInInt;
		repaint();
	}
	

	public int getP2ValueInInt() {
		return P2ValueInInt;
	}
	

	public void setP2ValueInInt(int p2ValueInInt) {
		P2ValueInInt = p2ValueInInt;
		repaint();
	}
	

	public Card getP1WonWithHighCardOf() {
		return p1WonWithHighCardOf;
	}
	

	public void setP1WonWithHighCardOf(Card p1WonWithHighCardOf) {
		this.p1WonWithHighCardOf = p1WonWithHighCardOf;
		repaint();
	}
	

	public Card getP2WonWithHighCardOf() {
		return p2WonWithHighCardOf;
	}
	

	public void setP2WonWithHighCardOf(Card p2WonWithHighCardOf) {
		this.p2WonWithHighCardOf = p2WonWithHighCardOf;
		repaint();
	}
}