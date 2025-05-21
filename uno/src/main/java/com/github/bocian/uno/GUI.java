package com.github.bocian.uno;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class GUI extends Application
{
    public static PlayerData playerData;
    public static int currentCardIndex = 0;
    public static GUI instance;

    @FXML
    private Button playCardButton;
    @FXML
    private Button drawCardButton;
    @FXML
    private Button swipeLeftButton;
    @FXML
    private Button swipeRightButton;
    @FXML
    private Rectangle myCardRect;
    @FXML
    private Rectangle pileCardRect;
    @FXML
    private Label myCardLabel;
    @FXML
    private Label pileCardLabel;
    @FXML
    private Label cardCountLabel;
    

    private Card getPileCard() { return playerData.getCardOnPile(); }
    private Card getCurrentCard() { return playerData.getHand().get(currentCardIndex); }
    @FXML	
    public void updateCardCount() { cardCountLabel.setText(currentCardIndex+1 + "/" + playerData.getHand().size()); }
    public void setCardCount(int left,int right) { cardCountLabel.setText(left+1 + "/" + right); }
    @FXML
    private void playACard() { playerData.getClient().playACard(playerData.getHand().get(currentCardIndex)); }
    private void setCardColor(Rectangle rect,Card card) { rect.setFill(getColor(card)); }
    
    
    public GUI()
    {
    	//TODO
    }
    
    
    @FXML
    public void test()
    {
    	//TODO
    }
    
    
    @FXML
    private void switchToRightCard()
    {
        int handSize = playerData.getHand().size();
        if(currentCardIndex+1 >= handSize)  currentCardIndex = 0;
        else 								currentCardIndex++;

        updateCardCount();
        printCurrentCard();
        setCardColor(myCardRect,getCurrentCard());
    }
    
    
    @FXML
    private void switchToLeftCard()
    {
        int handSize = playerData.getHand().size();
        if(handSize == 0) return;
        if(currentCardIndex-1 < 0) 	currentCardIndex = handSize-1;
        else 						currentCardIndex--;
        
        updateCardCount();
        printCurrentCard();
        setCardColor(myCardRect,getCurrentCard());
    }

    
    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/uno/hello-view.fxml"));
        Parent root = loader.load();
        instance = loader.getController();

        stage.setTitle("UNO GUI");
        stage.setScene(new Scene(root, 1100, 650));
        stage.show();
    }
    
   
    @FXML
    public void updatePileCard()
    {
        System.out.println(getPileCard().toString());
        Platform.runLater(() -> {pileCardLabel.setText(getPileCard().toString()) ; setCardColor(pileCardRect,getPileCard());});
    }

    
    public void changeToNearest()
    {
        if(currentCardIndex <= 0) 	currentCardIndex = 0;
        else 						currentCardIndex--;

        setCardColor(myCardRect,getCurrentCard());
        Platform.runLater(this::printCurrentCard);
    }
    
    private void printCurrentCard()
    {
        if(playerData.getHand().size() <= 0)
        {
            System.out.println("reka jest pusta");
        }
        else
        {
            String cardData = getCurrentCard().toString();
            myCardLabel.setText(cardData);
        }
    }
    
    
    @FXML
    private void requestACard()
    {
        setCardCount(currentCardIndex,playerData.getHand().size()+1);
        playerData.getClient().drawACard();

        if(playerData.getHand().isEmpty()) printCurrentCard();
    }
    
    
    javafx.scene.paint.Color getColor(Card card)
    {
        return switch(card.color)
        {
            case red -> Color.RED;
            case blue -> Color.LIGHTBLUE;
            case yellow -> Color.YELLOW;
            case green -> Color.LIGHTGREEN;
            case noColor -> Color.GREY;
            default -> Color.PINK;
        };
    }
}
