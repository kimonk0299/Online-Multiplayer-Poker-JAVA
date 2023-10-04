package pokerapplication;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class PokerClient extends JFrame implements Runnable, PokerConstant {
	
	public String player1= "*player1*";
	public String player2= "player2";

	private boolean myTurn = false;
	private boolean waiting = true;
	private boolean continueToPlay = true;
	
	private int playerAction;
	private double raisedAmount;
	private int turnTakenThisRound = 0; 
	

	
	//Streams 
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;
	
	//buttons
	private JButton check;
	private JButton fold;
	private JButton raise;
	private JTextField raisedInputBox;
	private String playername;

	
	
	//player chips
	double p1Chips = 1000;
	double p2Chips = 1000;
	
	Card[] myHand;
	Card[] flopHand;
	
	//this is used after game ends to show opponents hand cards
	Card[] p1Hand;
	Card[] p2Hand;
	TablePanel s;
	private JTextArea textArea = new JTextArea();
	private JLabel totalChips = new JLabel();
	private JLabel labelStatus = new JLabel();
	private JLabel labelTitle = new JLabel();

	public PokerClient(String title, String playername) throws IOException{
		super(title);
		this.playername=playername;
		myHand = new Card[2];
		flopHand = new Card[5];
		JPanel chatpanel= new JPanel();
		ChatClient c= new ChatClient(playername);
		System.out.print("OUT OF RUN");
		chatpanel.add(c);
		
		s = new TablePanel(this);
		int currentCount = Counter.getCount();
		System.out.print("COUNT:"+currentCount);
		
		if (currentCount== 0) {
		s.setPlayerName(playername);
		Counter.increment();
		}
		
		else {
			System.out.print(playername);
			s.setPlayerName2(playername);
		}
		
		setBounds(100, 100, 1701, 1600);

		setLayout(new BorderLayout());
		add(s, BorderLayout.CENTER);
		add(totalChips, BorderLayout.NORTH);
		add(labelTitle,BorderLayout.NORTH);
		add(chatpanel,  BorderLayout.EAST);
		add(gameControlPanel(), BorderLayout.SOUTH);
		textArea.setRows(7);
		textArea.setEditable(false);
		labelTitle.setText("title here");
		labelStatus.setText("status here");
	
		connectToServer(playername);
	}
	

	public JPanel gameControlPanel(){
		JPanel gameControlPanel = new JPanel();
		
		
		check = new JButton("Check");
		check.setPreferredSize(new Dimension(80,25));
	   	check.addActionListener(new checkListener());
	   		
        fold = new JButton("Fold");
        fold.setPreferredSize(new Dimension(80,25));
   		fold.addActionListener(new foldListener());
   		
        raise = new JButton("Raise");
        raise.setPreferredSize(new Dimension(80,25));
   		raise.addActionListener(new raiseListener());
   		
		
   		raisedInputBox = new JTextField();
   		raisedInputBox.setPreferredSize(new Dimension(80,25));
   		
   		gameControlPanel.setBackground(Color.GRAY);
	 	gameControlPanel.setLayout(new GridBagLayout());
	 	GridBagConstraints gbc = new GridBagConstraints();
	 	gbc.insets = new Insets(15,15,15,15); //15px
	 	
	 	gbc.gridx = 0;
		gbc.gridy = 0;
		gameControlPanel.add(check, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gameControlPanel.add(fold, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gameControlPanel.add(raisedInputBox, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 0;
		gameControlPanel.add(raise, gbc);
		

	    
	    return gameControlPanel;
		
	}

	private void connectToServer(String playername) throws IOException{
		//Socket socket;
		try {
			
			Socket socket = new Socket(InetAddress.getLocalHost(), 8000);
			//inputstream from server
			fromServer = new ObjectInputStream(socket.getInputStream());
			
			//outputstream to server
			toServer = new ObjectOutputStream(socket.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread thread = new Thread(this);
		thread.start();
		
	}

	public void run(){
		
		try{
			
			//get notification from server
			int player = fromServer.readInt();
			
			if(player == PLAYER1){
				
				setTitleMessage("Player: " + playername);
				s.setP1TotalChips(p1Chips);
				s.setCurrentPlayerIs(PLAYER1);
				disableButtons();
				s.setCurrentGameRound(WAITINGROUND);
				toServer.writeObject(s.getPlayerName());
				toServer.flush();
				
				//player2 name recieved from server
				String tempP2Name = (String)fromServer.readObject();
				s.setPlayerName2(tempP2Name);
				s.setBubbleStatus(s.getPlayerName() + " Turn!");
				//myHand cards recieved
				Card object[] = (Card[])fromServer.readObject(); 
				myHand = (Card[])object;
				p1Hand = (Card[])object;
				
				//recieve p2 hand card 
				Card object2[] = (Card[])fromServer.readObject();
				p2Hand= (Card[])object2;
				
				s.setCurrentGameRound(PREFLOPROUND);
				//s.setPlayerName2("player-2");
				s.setP2TotalChips(p2Chips);
				enableButtons();
				s.setP1Card1(myHand[0]);
				s.setP1Card2(myHand[1]);
				s.setP2Card1(p2Hand[0]);
				s.setP2Card2(p2Hand[1]);
				displayAll(myHand);
				
				
				//display flopHand
				
				Card object1[] = (Card[])fromServer.readObject(); 
				flopHand = (Card[])object1;
				//setStatusMessage("flop displayed");
				
				s.setFlop1(flopHand[0]);
				s.setFlop2(flopHand[1]);
				s.setFlop3(flopHand[2]);
				s.setFlop4(flopHand[3]);
				s.setFlop5(flopHand[4]);
				
				displayAll(flopHand);
				

				
				setMyTurn(true);
			}else if(player == PLAYER2){
				
				setTitleMessage("Player: " + playername);
				s.setP2TotalChips(p2Chips);
				s.setCurrentPlayerIs(PLAYER2);

				s.setP1TotalChips(p1Chips);
				
				
				toServer.writeObject(s.getPlayerName2());
				toServer.flush();
				
				//player1 name recieved from server
				String tempP1Name = (String)fromServer.readObject();
				s.setPlayerName(tempP1Name);
				s.setBubbleStatus(s.getPlayerName() + " Turn!");
				s.setCurrentGameRound(PREFLOPROUND);
				//hand card recieved
				Card object[] = (Card[])fromServer.readObject(); 
				myHand = (Card[])object;
				p2Hand = (Card[])object;
				//recieve p1 hand card 
				Card object2[] = (Card[])fromServer.readObject();
				p1Hand= (Card[])object2;
				
				s.setP2Card1(myHand[0]);
				s.setP2Card2(myHand[1]);
				s.setP1Card1(p1Hand[0]);
				s.setP1Card2(p1Hand[1]);
				displayAll(myHand);
				
				//flopHand recieved
				Card object1[] = (Card[])fromServer.readObject(); 
				flopHand = (Card[])object1;
				
				
				s.setFlop1(flopHand[0]);
				s.setFlop2(flopHand[1]);
				s.setFlop3(flopHand[2]);
				s.setFlop4(flopHand[3]);
				s.setFlop5(flopHand[4]);
				
				displayAll(flopHand);
				
			}
			
			while(continueToPlay){
				
			
				
				if(player == PLAYER1){
						s.setPlayerToken(1);
						checkSetButtonName();
						
						waitForPlayerAction(); // Wait for player 1 to move
				        sendMove(); // Send the move to the server
				        sendChipsDetail();
				        
				        receiveInfoFromServer(); // Receive info from the server
				}else if(player == PLAYER2){
						s.setPlayerToken(2);
						checkSetButtonName();
						disableButtons();
						receiveInfoFromServer(); // Receive info from the server
						checkSetButtonName();
				        waitForPlayerAction(); // Wait for player 2 to move
				        sendMove(); // Send player 2's move to the server
				        sendChipsDetail();
				        
					}
				}
			
			
		}catch(Exception ex){
			
		}
	}
	

   private void waitForPlayerAction() throws InterruptedException {

	   while (isWaiting()) { 
		      Thread.sleep(100);
	   }
	    setWaiting(true);
   }
   


   
   private void sendMove() throws IOException {
	   	   toServer.writeInt(s.getCurrentGameRound());
	   	   toServer.flush();
		   toServer.writeInt(getPlayerAction()); 
		   toServer.flush();
		   toServer.writeDouble(s.getCurrentBidToMatch());
		   toServer.flush();
		   toServer.writeDouble(getRaisedAmount());
		   toServer.flush();
		   toServer.writeInt(getTurnTakenThisRound());
		   toServer.flush();
	}

   private void sendChipsDetail()throws IOException{
	   toServer.writeDouble(s.getCurrentPot());
	   toServer.flush();
	   toServer.writeDouble(s.getP1TotalChipsBid());
	   toServer.flush();
	   toServer.writeDouble(s.getP2TotalChipsBid());
	   toServer.flush();
	   toServer.writeDouble(s.getP1TotalChips());
	   toServer.flush();
	   toServer.writeDouble(s.getP2TotalChips());
	   toServer.flush();
   }

   private void receiveInfoFromServer() throws IOException, ClassNotFoundException {
	   
	    // Receive game status - this will either be a win message, a draw message or a continue message
	    int status = fromServer.readInt();
	    
	    if (status == PLAYER1_WON) {
	    
	    	int p1ValueInIntRecieved = fromServer.readInt();
	    	s.setP1ValueInInt(p1ValueInIntRecieved);
	    	
	    	Card wonWithHighCardRecieved = (Card)fromServer.readObject();
	    	s.setP1WonWithHighCardOf(wonWithHighCardRecieved);
	    	
	    	s.setWinnerIs(s.getPlayerName() + " Wins! "+ getHandValueInString(s.getP1ValueInInt())+ ", high card of: "+s.getP1WonWithHighCardOf().toString());
	    	
	    	receiveMove();
		    recieveChipsUpdate();
		    s.setP1TotalChips(s.getP1TotalChips() + s.getCurrentPot());
		    s.setP1TotalChipsBid(0);
		    s.setP2TotalChipsBid(0);
		    s.setCurrentPot(0);
		    s.setBubbleStatus(s.getPlayerName() + " Wins!!");
		    continueToPlay = false;
		   
		    
	    }
	    else if (status == PLAYER2_WON) {
	    	int p2ValueInIntRecieved = fromServer.readInt();
	    	s.setP2ValueInInt(p2ValueInIntRecieved);
	    	
	    	Card wonWithHighCardRecieved = (Card)fromServer.readObject();
	    	s.setP2WonWithHighCardOf(wonWithHighCardRecieved);
	    	
	    	s.setWinnerIs(s.getPlayerName2() + " Wins! "+ getHandValueInString(s.getP2ValueInInt())+ ", high card of: "+s.getP2WonWithHighCardOf().toString());
	    	
	    	  receiveMove();
		      recieveChipsUpdate();
		      s.setP2TotalChips(s.getP2TotalChips() + s.getCurrentPot());
		      s.setP1TotalChipsBid(0);
			  s.setP2TotalChipsBid(0);
			  s.setCurrentPot(0);
			  s.setBubbleStatus(s.getPlayerName2() + " Wins!!");
			  continueToPlay = false;
		     
	    }
	    else if (status == PLAYER1_FOLD) {
	      continueToPlay = false;
	      receiveMove();
	      recieveChipsUpdate();
	    }else if(status == PLAYER2_FOLD){
	      continueToPlay = false;
	      receiveMove();
	      recieveChipsUpdate();
	    }else {
	      receiveMove();
	      recieveChipsUpdate();
	      setMyTurn(true);
	      enableButtons();
	    }
    }
   

    private void receiveMove() throws IOException {
	    // Get the other player's move
    		int currentRoundRecieved = fromServer.readInt();
    		s.setCurrentGameRound(currentRoundRecieved);
    		int action = fromServer.readInt();
    		playerAction = action;
    		
    		double currentBidToMatchRecieved = fromServer.readDouble();
    		s.setCurrentBidToMatch(currentBidToMatchRecieved);
    		int turnTakenRecieved = fromServer.readInt();
    		setTurnTakenThisRound(turnTakenRecieved);

    		//bubble status = folds
			if(getPlayerAction() == PLAYER1_FOLD){
	    		s.setBubbleStatus(s.getPlayerName() + " Folds!");
	    		s.setWinnerIs(s.getPlayerName2() + " wins the pot!");
	    	}else if(getPlayerAction() == PLAYER2_FOLD){
	    		s.setBubbleStatus(s.getPlayerName2() + " Folds!");
	    		s.setWinnerIs(s.getPlayerName() + " wins the pot!");
	    	}
    
    }
    

    private void recieveChipsUpdate() throws IOException{
    	
    	double currPotRecieved = fromServer.readDouble();
    	s.setCurrentPot(currPotRecieved);
    	
    	double p1TotalChipsBidRecieved = fromServer.readDouble();
    	s.setP1TotalChipsBid(p1TotalChipsBidRecieved);
  
    	double p2TotalChipsBidRecieved = fromServer.readDouble();
    	s.setP2TotalChipsBid(p2TotalChipsBidRecieved);
    	
    
    	double p1TotalChipsRecieved = fromServer.readDouble();
    	s.setP1TotalChips(p1TotalChipsRecieved);
  
    	
    	double p2TotalChipsRecieved = fromServer.readDouble();
    	s.setP2TotalChips(p2TotalChipsRecieved);  
    	//bubble status outputs bets or raised 
    	if(s.getPlayerToken() == 1){
    		if(playerAction == RAISE && s.getP1TotalChipsBid() == 0){
	    		 s.setBubbleStatus(s.getPlayerName2() + " Bets!");
	    	 }else if(playerAction == RAISE){
	    		 s.setBubbleStatus(s.getPlayerName2() + " raised!");
	    	 }
    		
    		if(playerAction == CHECK && s.getCurrentBidToMatch() == 0){
	    		 s.setBubbleStatus(s.getPlayerName2() + " Checks!");
	    	}else if(playerAction == CHECK){
	    		 s.setBubbleStatus(s.getPlayerName2() + " Calls!");
	    	}
		}else if(s.getPlayerToken() == 2){
			if(playerAction == RAISE && s.getP2TotalChipsBid() == 0){
	    		 s.setBubbleStatus(s.getPlayerName() + " Bets!");
	    	 }else if(playerAction == RAISE){
	    		 s.setBubbleStatus(s.getPlayerName() + " raised!");
	    	 }
			
			if(playerAction == CHECK && s.getCurrentBidToMatch() == 0){
	    		 s.setBubbleStatus(s.getPlayerName() + " Checks!");
	    	}else if(playerAction == CHECK){
	    		 s.setBubbleStatus(s.getPlayerName() + " Calls!");
	        }
    	}
    	
    	//bubble status = folds
		if(playerAction == PLAYER1_FOLD){
    		s.setBubbleStatus(s.getPlayerName() + " Folds!");
    	}else if(playerAction == PLAYER2_FOLD){
    		s.setBubbleStatus(s.getPlayerName2() + " Folds!");
    	}
		
    	if(s.getPlayerToken() == 1){
    		if(s.getP2TotalChips() == 0){
   			 s.setBubbleStatus(s.getPlayerName2() + " is All in !");
   		 	}
    	}else if(s.getPlayerToken() == 2){
    	  	if(s.getP1TotalChips() == 0){
   			 s.setBubbleStatus(s.getPlayerName() + " is All in !");
    	  	}
    	}
    }
    	

    public void setTurnTakenThisRound(int turnTaken){
    	turnTakenThisRound = turnTaken;
    }
    

    public int getTurnTakenThisRound(){
    	return turnTakenThisRound;
    }
    

	public void setTitleMessage(String msg){
		labelTitle.setText(msg);
	}
	
	

	public void setPlayerAction(int action){
		playerAction = action;
	}
	

	public int getPlayerAction(){
		return playerAction;
	}
	

	public boolean isWaiting() {
	    return waiting;
    }
	

	public void setWaiting(boolean b) {
		  waiting = b;
    }
	


	

	public String getHandValueInString(int pValue){
		String temp = null;
		switch(pValue){
			case 1:
				temp = "High Card";
			break;
			case 2:
				temp = "with a pair";
			break;
			case 3:
				temp = "with 2 pairs";
			break;
			case 4:
				temp = "with 3 of a kind";
			break;
			case 5:
				temp = "with a straight";
			break;
			case 6:
				temp = "with a flush";
			break;
			case 7:
				temp = "with a full house";
			break;
			case 8:
				temp = "with 4 of a kind";
			break;
			case 9:
				temp = "with a straight flush";
			break;
			case 10:
				temp = "with a Royal flush";
			break;
			default:
				temp = "none of the value matched.";
		}
		return temp;	
	}
	
	//delet this maybe =----------------------->>>>>>>>>>>>>>>>
	public void displayAll(Card[] cardArray){
		//sortByRank(myHand);
		for(int i=0; i<cardArray.length; i++){
			//textArea.append("index of " + i + " : " +myHand[i] +"\n");
			textArea.append(cardArray[i].toString()+"\n");
		}
		//valueHand(cards);
	}
	

	public void disableButtons(){
		check.setEnabled(false);
		fold.setEnabled(false);
		raise.setEnabled(false);
		raisedInputBox.setEditable(false);
	}
	

	public void enableButtons(){
		check.setEnabled(true);
		fold.setEnabled(true);
		raise.setEnabled(true);
		raisedInputBox.setEditable(true);
	}
	

	public double getRaisedAmount() {
		return raisedAmount;
	}
	

	public void setRaisedAmount(double raisedAmount) {
		this.raisedAmount = raisedAmount;
	}
	

	public boolean isMyTurn() {
		return myTurn;
	}
	

	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}
	

	public void checkSetButtonName(){
			if(s.getCurrentPot() <= 0){ //s.getCurrentGameRound() == PREFLOPROUND &&
				raise.setText("Bet");
			}else{
				raise.setText("Raise");
			}
			if(s.getP1TotalChipsBid() != s.getP2TotalChipsBid()){
				check.setText("Call");
			}else{
				check.setText("Check");
			}

	}
	

	private class checkListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			setPlayerAction(CHECK);
			setMyTurn(false);
			setWaiting(false);
			disableButtons();
			if(s.getPlayerToken() == 1){
		    	  //updating the poker chips
		    	 s.setP1TotalChips(s.getP1TotalChips() - s.getCurrentBidToMatch());
		    	 s.setP1TotalChipsBid(s.getP1TotalChipsBid() + s.getCurrentBidToMatch());
		    	 s.setCurrentPot(s.getCurrentPot()+s.getCurrentBidToMatch());
		    	 s.setCurrentBidToMatch(0);
		    	 

		    	 if(check.getText() == "Check"){
		    		 s.setBubbleStatus(s.getPlayerName() + " Checks!");
		    	 }else{
		    		 s.setBubbleStatus(s.getPlayerName() + " Calls!");
		    	 }
		    	 //s.setBubbleStatus(s.getPlayerName2()+" turn!");
		      }else if(s.getPlayerToken() == 2){
		    	
		    	  //updating the poker chips
			     s.setP2TotalChips(s.getP2TotalChips() - s.getCurrentBidToMatch());     
			     s.setP2TotalChipsBid(s.getP2TotalChipsBid() + s.getCurrentBidToMatch());
			     s.setCurrentPot(s.getCurrentPot()+s.getCurrentBidToMatch());
			     s.setCurrentBidToMatch(0);
			     
			     //s.setCurrentGameRound(s.getCurrentGameRound());
			     //s.setBubbleStatus(s.getPlayerName()+" turn!");
			     if(check.getText() == "Check"){
		    		 s.setBubbleStatus(s.getPlayerName2() + " Checks!");
		    	 }else{
		    		 s.setBubbleStatus(s.getPlayerName2() + " Calls!");
		    	 }
		      }
			
			if(getTurnTakenThisRound() >= 1){
				if(s.getP1TotalChipsBid() == s.getP2TotalChipsBid()){
					 s.setCurrentGameRound(s.getCurrentGameRound() + 1);
					 setTurnTakenThisRound(0);
				}
			}else{
				 s.setCurrentGameRound(s.getCurrentGameRound());
				 setTurnTakenThisRound(getTurnTakenThisRound() + 1);
			}
			
			 if(s.getP1TotalChips() == 0 && s.getP2TotalChips() == 0){
				 s.setCurrentGameRound(FINDWINNERROUND);
			 }
		}
	}
	

	private class foldListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			if(s.getPlayerToken() == 1){
				setPlayerAction(PLAYER1_FOLD);
				s.setP2TotalChips(s.getP2TotalChips() + s.getCurrentPot());     
			    s.setP2TotalChipsBid(0);
			    //s.setP1TotalChips(s.getP1TotalChips() - s.getCurrentPot());
			    s.setP1TotalChipsBid(0);
			}else if(s.getPlayerToken() == 2){
				setPlayerAction(PLAYER2_FOLD);
				s.setP1TotalChips(s.getP1TotalChips() + s.getCurrentPot());     
			    s.setP1TotalChipsBid(0);
			    //s.setP2TotalChips(s.getP2TotalChips() - s.getCurrentPot());
			    s.setP2TotalChipsBid(0);
			}
			setMyTurn(false);
			setWaiting(false);
			disableButtons();
			//setMyTurn(false);
			//setWaiting(false);
			//disableButtons();
			s.setCurrentGameRound(FOLDROUND);
			setTurnTakenThisRound(0);
			s.setCurrentPot(0);
		    s.setCurrentBidToMatch(0);
		    setRaisedAmount(0);
		  //bubble status = folds
			if(getPlayerAction() == PLAYER1_FOLD){
	    		s.setBubbleStatus(s.getPlayerName() + " Folds!");
	    		s.setWinnerIs(s.getPlayerName2() + " wins the pot!");
	    	}else if(getPlayerAction() == PLAYER2_FOLD){
	    		s.setBubbleStatus(s.getPlayerName2() + " Folds!");
	    		s.setWinnerIs(s.getPlayerName() + " wins the pot!");
	    	}
		}
	}
	

	private class raiseListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			if(raisedInputBox.getText().equals("")){
				JOptionPane.showMessageDialog(null, "Input "+raise.getText()+" amount first!");
			}else if(s.getPlayerToken() == 1){
				
				double tempRaisedAmountCheck = Double.parseDouble(raisedInputBox.getText());
				if(tempRaisedAmountCheck > s.getP1TotalChips()){
					JOptionPane.showMessageDialog(null, "Player 1, You do not have enough chips");
					
				}else if(s.getP1TotalChipsBid() + tempRaisedAmountCheck <= s.getP2TotalChipsBid() && tempRaisedAmountCheck != s.getP1TotalChips()){		
					JOptionPane.showMessageDialog(null, "Player 1, You need to raise more to match the bids");					
				}else{
					
					double tempRaisedAmount = Double.parseDouble(raisedInputBox.getText());
					setRaisedAmount(getRaisedAmount() + tempRaisedAmount );
					setPlayerAction(RAISE);
					setMyTurn(false);
					setWaiting(false);
					disableButtons();
					
					 s.setP1TotalChips(s.getP1TotalChips() - getRaisedAmount());
			    	 s.setP1TotalChipsBid(s.getP1TotalChipsBid() + getRaisedAmount());
			    	 s.setCurrentPot(s.getCurrentPot()+getRaisedAmount());		   
			    	 s.setCurrentBidToMatch(s.getP1TotalChipsBid() - s.getP2TotalChipsBid());
			    	 if(raise.getText() == "Bet"){
			    		 s.setBubbleStatus(s.getPlayerName() + " Bets!");
			    	 }else{
			    		 s.setBubbleStatus(s.getPlayerName() + " raised!");
			    	 }
			    	 if(s.getP1TotalChips() == 0){
						 s.setBubbleStatus(s.getPlayerName() + " is All in !");
					 }
			    	 setTurnTakenThisRound(getTurnTakenThisRound() + 1);
				}
			}else if(s.getPlayerToken() == 2){
				double tempRaisedAmountCheck = Double.parseDouble(raisedInputBox.getText());
				if(tempRaisedAmountCheck > s.getP2TotalChips()){
					JOptionPane.showMessageDialog(null, "Player 2, You do not have enough chips");
				}else if(s.getP2TotalChipsBid() + tempRaisedAmountCheck <= s.getP1TotalChipsBid()&& tempRaisedAmountCheck != s.getP2TotalChips()){
					JOptionPane.showMessageDialog(null, "Player 2, You need to raise more to match the bids");
				}else{
					double tempRaisedAmount = Double.parseDouble(raisedInputBox.getText());
					setRaisedAmount(getRaisedAmount() + tempRaisedAmount );
					setPlayerAction(RAISE);
					setMyTurn(false);
					setWaiting(false);
					disableButtons();
					
					 s.setP2TotalChips(s.getP2TotalChips() - getRaisedAmount());     
				     s.setP2TotalChipsBid(s.getP2TotalChipsBid() + getRaisedAmount());
				     s.setCurrentPot(s.getCurrentPot()+ getRaisedAmount());	
					 s.setCurrentBidToMatch(s.getP2TotalChipsBid() - s.getP1TotalChipsBid());
					 if(raise.getText() == "Bet"){
			    		 s.setBubbleStatus(s.getPlayerName2() + " Bets!");
			    	 }else{
			    		 s.setBubbleStatus(s.getPlayerName2() + " raised!");
			    	 }
					 if(s.getP2TotalChips() == 0){
						 s.setBubbleStatus(s.getPlayerName2() + " is All in !");
					 }
					 setTurnTakenThisRound(getTurnTakenThisRound() + 1);
				}
			}
			 raisedInputBox.setText("");
			 setRaisedAmount(0);
			 if(s.getP1TotalChips() == 0 && s.getP2TotalChips() == 0){
				 s.setCurrentGameRound(FINDWINNERROUND);
			 }else{
				 s.setCurrentGameRound(s.getCurrentGameRound());
			 }
		     
			
			
		}
	}
	
}//end of poker client