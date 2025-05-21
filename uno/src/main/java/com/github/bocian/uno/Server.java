package com.github.bocian.uno;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable
{
    public final static int PORT = 5000;
    private final int MAX_PLAYERS = 4;

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
    }

    @Override
    public void run()
    {
        try 
        {
            server = new ServerSocket(PORT);
            System.out.println("server został załączony na porcie: " + PORT);

            pool = Executors.newCachedThreadPool();
            while (!done)
            {
                Socket client = server.accept();
                if (connections.size() >= MAX_PLAYERS)
                {
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println("Serwer osiągnął limit połączeń. Spróbuj później.");
                    client.close();
                    continue;
                }

                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (Exception e) {
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
                broadcastCardOnPile();
                Request request;
                while((request = (Request)in.readObject()) != null)
                {
                    switch(request.getCommand())
                    {
                        case DRAW_CARD ->
                        {
                            System.out.println("otrzymano prosbe o karte");
                            Card card = gameData.drawCard();

                            Response response = new Response(card,Command.DRAW_CARD);
                            sendResponse(response);
                        }
                        case PLAY_CARD ->
                        {
                            Card playedCard = (Card)request.getPayload();
                            if(isPlayedCardLegal(playedCard))
                            {
                                gameData.throwCardOnPile(playedCard);
                                broadcastCardOnPile();
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
                    }
                }
            }

            catch(Exception e)
            {
                //handle
                System.out.println("rozlaczono klienta");
                shutdown();
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
