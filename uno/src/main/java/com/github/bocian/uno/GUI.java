package com.github.bocian.uno;

import java.util.EnumMap;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXML;

public class GUI extends Application
{
    public static PlayerData playerData;
    public static int currentCardIndex = 0;
    public static GUI instance;
	static Map<Assets, Image> imageMap;

    @FXML
    private Button playCardButton;
    @FXML
    private Button drawCardButton;
    @FXML
    private Button swipeLeftButton;
    @FXML
    private Button swipeRightButton;
    @FXML
    private ImageView myCardIMG;
    @FXML
    private ImageView pileCardIMG;
    @FXML
    private Label cardCountLabel;
    

    private Card getPileCard() { return playerData.getCardOnPile(); }
    private Card getCurrentCard() { return playerData.getHand().get(currentCardIndex); }
    @FXML	
    public void updateCardCount() { cardCountLabel.setText(currentCardIndex+1 + "/" + playerData.getHand().size()); }
    public void setCardCount(int left,int right) { cardCountLabel.setText(left+1 + "/" + right); }

    public static void setPlayerData(PlayerData data) { playerData = data; }
    
    private void setCard(ImageView img,Card card) 
    { 
    	try {
            Assets assetKey = Assets.valueOf(card.toString());
            img.setImage(imageMap.getOrDefault(assetKey, new Image("/com/github/bocian/assets/error.png")));
        } catch (IllegalArgumentException e) {
            System.err.println("Card string not found in Assets enum: " + card);
            img.setImage(new Image("/com/github/bocian/assets/error.png"));
        }
    }
    
    
    public GUI(){}
    
    

    
    
    @FXML
    private void switchToRightCard()
    {
        int handSize = playerData.getHand().size();
        if(currentCardIndex+1 >= handSize) currentCardIndex = 0;
        else currentCardIndex++;

        updateCardCount();
        printCurrentCard();
        setCard(myCardIMG,getCurrentCard());
        Logger.logEvent("Changed card selection");
    }
    
    
    @FXML
    private void switchToLeftCard()
    {
        int handSize = playerData.getHand().size();
        if(handSize == 0) return;
        
        if(currentCardIndex-1 < 0) currentCardIndex = handSize-1;
        else currentCardIndex--;
        
        updateCardCount();
        printCurrentCard();
        setCard(myCardIMG,getCurrentCard());
        Logger.logEvent("Changed card selection");
    }

    
    @Override
    public void start(Stage stage) throws Exception
    {
    	loadAssets();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/bocian/hello-view.fxml"));
        Parent root = loader.load();
        instance = loader.getController();

        stage.setTitle("UNO GUI");
        stage.setScene(new Scene(root, 1100, 650));
        stage.show();
        Logger.logEvent("GUI initialised succesfully");
    }
    
   
    @FXML
    public void updatePileCard()
    {
        System.out.println(getPileCard().toString());
        Platform.runLater(() -> {
        	setCard(pileCardIMG,getPileCard());
        	}
        );
        Logger.logEvent("Updated card pile");
    }
    public void showFirstCard(Card card)
    {
        setCard(myCardIMG,card);
    }
    public void updateCardCountText()
    {
        Platform.runLater(this::updateCardCount);
    }
    
    public void changeToNearest()
    {
        if(currentCardIndex <= 0) currentCardIndex = 0;
        else currentCardIndex--;

        setCard(myCardIMG,getCurrentCard());
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
        }
    }
    
    
    @FXML
    private void requestACard()
    {
        if(playerData.isMyTurn())
        {
            setCardCount(currentCardIndex,playerData.getHand().size()+1);
            playerData.getClient().drawACard();

            if(!playerData.getHand().isEmpty()) printCurrentCard();
            Logger.logEvent("Requested card from server");
        }
        else
        {
            System.out.println("nie można wykonać akcji - to nie twoja tura (GUI)");
        }

    }
    @FXML
    private void playACard()
    {
        if(playerData.isMyTurn())
        {
            playerData.getClient().playACard(playerData.getHand().get(currentCardIndex));
        }
        else
        {
            System.out.println("nie można wykonać akcji - to nie twoja tura (GUI)");
        }

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
    
    private static void loadAssets() {
		imageMap = new EnumMap<>(Assets.class);
		imageMap.put(Assets.red_zero, safeLoad("/com/github/bocian/assets/red_zero.png"));
		imageMap.put(Assets.red_one, safeLoad("/com/github/bocian/assets/red_one.png"));
		imageMap.put(Assets.red_two, safeLoad("/com/github/bocian/assets/red_two.png"));
		imageMap.put(Assets.red_three, safeLoad("/com/github/bocian/assets/red_three.png"));
		imageMap.put(Assets.red_four, safeLoad("/com/github/bocian/assets/red_four.png"));
		imageMap.put(Assets.red_five, safeLoad("/com/github/bocian/assets/red_five.png"));
		imageMap.put(Assets.red_six, safeLoad("/com/github/bocian/assets/red_six.png"));
		imageMap.put(Assets.red_seven, safeLoad("/com/github/bocian/assets/red_seven.png"));
		imageMap.put(Assets.red_eight, safeLoad("/com/github/bocian/assets/red_eight.png"));
		imageMap.put(Assets.red_nine, safeLoad("/com/github/bocian/assets/red_nine.png"));
		imageMap.put(Assets.red_stop, safeLoad("/com/github/bocian/assets/red_stop.png"));
		imageMap.put(Assets.red_swapTurn, safeLoad("/com/github/bocian/assets/red_swapTurn.png"));
		imageMap.put(Assets.red_plusTwo, safeLoad("/com/github/bocian/assets/red_plusTwo.png"));
		imageMap.put(Assets.blue_zero, safeLoad("/com/github/bocian/assets/blue_zero.png"));
		imageMap.put(Assets.blue_one, safeLoad("/com/github/bocian/assets/blue_one.png"));
		imageMap.put(Assets.blue_two, safeLoad("/com/github/bocian/assets/blue_two.png"));
		imageMap.put(Assets.blue_three, safeLoad("/com/github/bocian/assets/blue_three.png"));
		imageMap.put(Assets.blue_four, safeLoad("/com/github/bocian/assets/blue_four.png"));
		imageMap.put(Assets.blue_five, safeLoad("/com/github/bocian/assets/blue_five.png"));
		imageMap.put(Assets.blue_six, safeLoad("/com/github/bocian/assets/blue_six.png"));
		imageMap.put(Assets.blue_seven, safeLoad("/com/github/bocian/assets/blue_seven.png"));
		imageMap.put(Assets.blue_eight, safeLoad("/com/github/bocian/assets/blue_eight.png"));
		imageMap.put(Assets.blue_nine, safeLoad("/com/github/bocian/assets/blue_nine.png"));
		imageMap.put(Assets.blue_stop, safeLoad("/com/github/bocian/assets/blue_stop.png"));
		imageMap.put(Assets.blue_swapTurn, safeLoad("/com/github/bocian/assets/blue_swapTurn.png"));
		imageMap.put(Assets.blue_plusTwo, safeLoad("/com/github/bocian/assets/blue_plusTwo.png"));
		imageMap.put(Assets.green_zero, safeLoad("/com/github/bocian/assets/green_zero.png"));
		imageMap.put(Assets.green_one, safeLoad("/com/github/bocian/assets/green_one.png"));
		imageMap.put(Assets.green_two, safeLoad("/com/github/bocian/assets/green_two.png"));
		imageMap.put(Assets.green_three, safeLoad("/com/github/bocian/assets/green_three.png"));
		imageMap.put(Assets.green_four, safeLoad("/com/github/bocian/assets/green_four.png"));
		imageMap.put(Assets.green_five, safeLoad("/com/github/bocian/assets/green_five.png"));
		imageMap.put(Assets.green_six, safeLoad("/com/github/bocian/assets/green_six.png"));
		imageMap.put(Assets.green_seven, safeLoad("/com/github/bocian/assets/green_seven.png"));
		imageMap.put(Assets.green_eight, safeLoad("/com/github/bocian/assets/green_eight.png"));
		imageMap.put(Assets.green_nine, safeLoad("/com/github/bocian/assets/green_nine.png"));
		imageMap.put(Assets.green_stop, safeLoad("/com/github/bocian/assets/green_stop.png"));
		imageMap.put(Assets.green_swapTurn, safeLoad("/com/github/bocian/assets/green_swapTurn.png"));
		imageMap.put(Assets.green_plusTwo, safeLoad("/com/github/bocian/assets/green_plusTwo.png"));
		imageMap.put(Assets.yellow_zero, safeLoad("/com/github/bocian/assets/yellow_zero.png"));
		imageMap.put(Assets.yellow_one, safeLoad("/com/github/bocian/assets/yellow_one.png"));
		imageMap.put(Assets.yellow_two, safeLoad("/com/github/bocian/assets/yellow_two.png"));
		imageMap.put(Assets.yellow_three, safeLoad("/com/github/bocian/assets/yellow_three.png"));
		imageMap.put(Assets.yellow_four, safeLoad("/com/github/bocian/assets/yellow_four.png"));
		imageMap.put(Assets.yellow_five, safeLoad("/com/github/bocian/assets/yellow_five.png"));
		imageMap.put(Assets.yellow_six, safeLoad("/com/github/bocian/assets/yellow_six.png"));
		imageMap.put(Assets.yellow_seven, safeLoad("/com/github/bocian/assets/yellow_seven.png"));
		imageMap.put(Assets.yellow_eight, safeLoad("/com/github/bocian/assets/yellow_eight.png"));
		imageMap.put(Assets.yellow_nine, safeLoad("/com/github/bocian/assets/yellow_nine.png"));
		imageMap.put(Assets.yellow_stop, safeLoad("/com/github/bocian/assets/yellow_stop.png"));
		imageMap.put(Assets.yellow_swapTurn, safeLoad("/com/github/bocian/assets/yellow_swapTurn.png"));
		imageMap.put(Assets.yellow_plusTwo, safeLoad("/com/github/bocian/assets/yellow_plusTwo.png"));
		imageMap.put(Assets.noColor_wildCard, safeLoad("/com/github/bocian/assets/noColor_wildCard.png"));
		imageMap.put(Assets.noColor_plusFour, safeLoad("/com/github/bocian/assets/noColor_plusFour.png"));
		
        Logger.logEvent("Assets loaded");
	}
    
    private static Image safeLoad(String path) {
        var url = GUI.class.getResource(path);
        if (url == null) {
            Logger.logEvent("Missing asset: " + path);
            throw new IllegalArgumentException("Missing resource: " + path);
        }
        return new Image(url.toExternalForm());
    }
}
