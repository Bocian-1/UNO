package com.github.bocian.uno;

import javafx.application.Application;
import javafx.application.Platform;

import java.io.*;
import java.net.*;

public class Client implements Runnable
{
    PlayerData playerData;

    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean done = false;
    private boolean gameStarted = false;
    private boolean firstCard = true;
    public Card playedCard;
    
    public Client() {}
    
    @Override
    public void run()
    {
        try
        {
            client = new Socket("192.168.43.137",Server.PORT);
            System.out.println("połączono z serverem!");
            Logger.logEvent("Server connection established");
            playerData = new PlayerData(this);
            GUI.playerData = playerData;

            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());




            //odbior informacji od servera
            Response response;
            while((response = (Response)in.readObject()) != null)
            {
                if(!gameStarted)
                {
                    if(response.getCommand() == Command.START_GAME)
                    {
                        System.out.println("gra zaczeta!");
                        gameStarted = true;


                        while(GUI.instance == null)
                        {
                            System.out.println("czekam na GUI");
                            Logger.logEvent("Waiting for GUI...");
                            Thread.sleep(500);
                        }
                        sendRequestToServer(Request.getPileCard());
                        startInputThread();
                        drawStarterCards();
                        Thread.sleep(400); // czekanie az dobrane zostana karty
                    }
                }
                else
                {
                    switch (response.getCommand())
                    {
                        case TURN_UPDATE ->
                        {
                            playerData.setMyTurn(response.isSucceed());
                            if(response.isSucceed()) {
                            	GUI.instance.setTurnLabel();
                            }
                            else {
                            	GUI.instance.unsetTurnLabel();
                            }
                        }
                        case DRAW_CARD ->
                        {
                            Card drawedCard = (Card)response.getPayload();
                            playerData.drawCard(drawedCard);
                            Platform.runLater(() -> GUI.instance.updateCardCount());
                            if(firstCard)
                            {
                                Platform.runLater(() -> GUI.instance.showFirstCard(drawedCard));
                                firstCard = false;
                            }
                            Logger.logEvent("Card drawn");
                        }
                        case PLAY_CARD ->
                        {
                            if(response.isSucceed())
                            {
                                System.out.println("zagrono karte: " + playedCard.toString());
                                playerData.getHand().remove(playedCard);
                                if(playerData.getHand().size() == 0)
                                {
                                    Platform.runLater(() -> GUI.instance.showWinPopup());
                                    //GUI ODPALA OKIENKO ZE WYGRALES
                                }
                                GUI.instance.changeToNearest();
                                GUI.instance.updateCardCountText();
                                Logger.logEvent("Played  " + playedCard.toString());
                            }
                            else
                            {
                                System.out.println("nielegalny ruch");
                                Logger.logEvent("Illegal move!");
                            }
                        }
                        case GET_PILE_CARD ->
                        {
                            playerData.setCardOnPile((Card)response.getPayload());
                            System.out.println("karta na kupce: " + playerData.getCardOnPile().toString());
                            GUI.instance.updatePileCard();

                        }
                        default -> throw new IllegalArgumentException("Unexpected value: " + response.getCommand());
                    }
                }

            }
        }
        catch (Exception e)
        {
            System.out.println("rozłączono z serverem");
            Logger.logEvent("Server connection lost");
            shutdown();
        }
    }
    
    
    private void startInputThread()
    {
        InputHandler inHandler = new InputHandler();
        Thread t = new Thread(inHandler);
        t.start();
    }

    
    public void shutdown()
    {
        done = true;
        try {
            in.close();
            out.close();
            if(!client.isClosed())
            {
                client.close();
            }
        }
        catch (IOException e)
        {
            System.out.println("blad polaczenia");
            Logger.logEvent("Connection Error");
            //ignore
        }
    }
    
    
    public void sendRequestToServer(Request request) throws IOException
    {
        System.out.println("wyslano prosbe: " + request.getCommand());
        out.writeObject(request);
        out.flush();
    }

    
    public void playACard(Card card)
    {
        if(playerData.getHand().size() <=0)
        {
            System.out.println("pusta reka");
            return;
        }
        
        try
        {
            playedCard = card;
            sendRequestToServer(Request.playCard(playedCard));
        }
        catch (IOException e)
        {
           throw new RuntimeException(e);
        }
    }
    
    
    private void drawStarterCards()
    {
        for(int i = 0;i<7;i++)
        {
            drawACard();
        }
    }
    
    
    public void drawACard()
    {
        try
        {
            sendRequestToServer(Request.drawCard());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    class InputHandler implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while(!done)
                {
                    String message = inReader.readLine();
                    
                    switch (message)
                    {
                        case "1" -> drawACard();
                        case "2" -> playACard(playerData.getHand().getLast());
                        case "3" ->
                        {
                            for(Card card : playerData.getHand())
                            {
                                System.out.println(card.toString());
                            }
                        }
                        case "4" ->
                        {
                            System.out.println(playerData.getCardOnPile().toString());

                        }
                        default -> System.out.println("nieprawidlowa komenda");
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println("blad polaczenia");
                shutdown();
            }
        }
    }

    public static void main(String[] args)
    {
        Client client = new Client();
        Thread thread = new Thread(client);
        thread.start();
        Application.launch(GUI.class);

    }
}
