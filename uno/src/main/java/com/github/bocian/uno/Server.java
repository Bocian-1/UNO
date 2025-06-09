package com.github.bocian.uno;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable
{
    public final static int PORT = 50000;
    private final int MAX_PLAYERS = 2;

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private ExecutorService pool;

    boolean done = false;

    GameData gameData;
    public Server()
    {
        connections = new ArrayList<>();
        gameData = new GameData();
        gameData.generateNewDeck();
        pool = Executors.newCachedThreadPool();

    }

    @Override
    public void run()
    {
        try 
        {
            server = new ServerSocket(PORT);
            System.out.println("server został załączony na porcie: " + PORT);
            Logger.logEvent("Server port reservation on port " + PORT);


            while (!done)
            {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
                if(connections.size() == MAX_PLAYERS)
                {
                    broadcastGameStart();
                    broadcastTurns(0);
                    System.out.println("gracze dolaczyli, gra zaczeta");
                    done = true;
                }
            }
        } catch (Exception e) {
            Logger.logEvent("Serer shutting down...");
            shutdown();
        }
    }

    //rozłącza server oraz wszystkich klientow
    public void shutdown()
    {
        try {
            done = true;
            if (!server.isClosed())
            {
                server.close();
            }
            for (ConnectionHandler handler : connections)
            {
                handler.shutdown();
            }
        }
        catch (Exception e)
        {
            // ignore
        }
    }
    
    void broadcastTurns(int playerTurnIndex)
    {
        for(int i = 0;i<connections.size();i++)
        {
            if(i == playerTurnIndex)
            {
                try {
                    connections.get(i).sendResponse(Response.turnUpdate(true));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    connections.get(i).sendResponse(Response.turnUpdate(false));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    void broadcastGameStart()
    {
        for(ConnectionHandler connection : connections)
        {
            try {
                connection.sendResponse(Response.startGame());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    void broadcastCardOnPile()
    {
        for(ConnectionHandler connection : connections)
        {
            try {
                connection.sendResponse(Response.sendPileCard(gameData.getCardOnPile()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    
    class ConnectionHandler implements Runnable
    {
        private Socket client;
        private ObjectOutputStream out;
        private ObjectInputStream in; //odbior od klienta
        private String nickname;

        public ConnectionHandler(Socket socket)
        {
            client = socket;
        }

        @Override
        public void run()
        {
            try
            {
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());

                Request request;
                while((request = (Request)in.readObject()) != null)
                {
                    switch(request.getCommand())
                    {
                        case GET_PILE_CARD ->
                        {
                            sendResponse(Response.sendPileCard(gameData.getCardOnPile()));
                        }
                        case DRAW_CARD ->
                        {
                            System.out.println("otrzymano prosbe o karte");
                            Card card = gameData.drawCard();

                            Response response = new Response(card,Command.DRAW_CARD);
                            broadcastTurns(gameData.getNextTurn(connections.size()));
                            sendResponse(response);
                        }
                        case PLAY_CARD ->
                        {
                            Card playedCard = (Card)request.getPayload();
                            if(isPlayedCardLegal(playedCard))
                            {
                                if(playedCard.value == Value.swapTurn) gameData.swapTurnDirections();
                                else if(playedCard.value == Value.plusTwo) sendCards(2);
                                else if(playedCard.value == Value.stop) gameData.getNextTurn(connections.size());
                                else if(playedCard.value == Value.plusFour)
                                {
                                    sendCards(4);
                                }
                                gameData.throwCardOnPile(playedCard);
                                broadcastCardOnPile();
                                broadcastTurns(gameData.getNextTurn(connections.size()));
                                sendResponse(Response.legalPlayedCard());
                            }
                            else
                            {
                                sendResponse(Response.illegalPlayedCard());
                            }


                            /*for(Card card : gameData.discardPile)
                            {
                                System.out.println(card.toString());
                            }*/
                        }
					default -> throw new IllegalArgumentException("Unexpected value: " + request.getCommand());
                    }
                }
            }

            catch(Exception e)
            {
                //handle
                System.out.println("rozlaczono klienta");
                Logger.logEvent("Client disconnected");
                shutdown();
            }
        }
        
        private void sendCards(int count) throws IOException
        {
            for(int i = 0;i<count;i++)
            {
                connections.get(gameData.getNextPlayer(connections.size())).sendResponse(new Response(gameData.drawCard(),Command.DRAW_CARD));
            }

        }
        private boolean isPlayedCardLegal(Card playedCard)
        {
            return Card.canPlay(gameData.getCardOnPile(),playedCard);
        }
        
        
        private void sendResponse(Response response) throws IOException
        {
            System.out.println("odsyłam odpowiedź");
            out.writeObject(response);
            out.flush();
        }

        //rozłacza klienta
        public void shutdown()
        {
            try {
                connections.remove(this);
                in.close();
                out.close();

                if (!client.isClosed())
                {
                    client.close();
                }
            } catch (Exception e)
            {
                // ignore
            }
        }
    }
    
    
    public static void main(String[] args)
    {
        Server server = new Server();
        server.run();
    }
}
