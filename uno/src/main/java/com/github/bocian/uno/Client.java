package com.github.bocian.uno;

import javafx.application.Application;

import java.io.*;
import java.net.*;

public class Client implements Runnable
{
    PlayerData playerData;

    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean done = false;
    public Card playedCard;

    @Override
    public void run()
    {
        try
        {
            client = new Socket("localhost",Server.PORT);
            System.out.println("połączono z serverem!");
            playerData = new PlayerData(this);
            GUI.playerData = playerData;

            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            startInputThread();
            drawStarterCards();

            //odbior informacji od servera
            Response response;
            while((response = (Response)in.readObject()) != null)
            {
                switch (response.getCommand())
                {
                    case DRAW_CARD ->
                    {
                        playerData.drawCard((Card)response.getPayload());
                    }
                    case PLAY_CARD ->
                    {
                        if(response.isSucceed())
                        {
                            System.out.println("zagrono karte: " + playedCard.toString());
                            playerData.getHand().remove(playedCard);
                            GUI.instance.changeToNearest();
                        }
                        else
                        {
                            System.out.println("nielegalny ruch");
                        }
                    }
                    case GET_PILE_CARD ->
                    {
                        playerData.setCardOnPile((Card)response.getPayload());
                        System.out.println("karta na kupce: " + playerData.getCardOnPile().toString());
                        /*
                        while(GUI.instance == null)
                        {
                            System.out.println("czekam na GUI");
                            Thread.sleep(100);
                        }
                        //GUI.instance.updatePileCard();
                        */
                    }
				default -> throw new IllegalArgumentException("Unexpected value: " + response.getCommand());
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("rozłączono z serverem");
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
        }
        else
        {
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
                            GUI.instance.test();
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

        //Application.launch(GUI.class);
    }
}
