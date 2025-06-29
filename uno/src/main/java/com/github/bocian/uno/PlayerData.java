package com.github.bocian.uno;

import java.util.ArrayList;
import java.util.List;

public class PlayerData
{
    private List<Card> hand;
    private boolean myTurn;
    private Client client;
    private Card cardOnPile;

    
    public void drawCard(Card card) { hand.add(card); }
    public List<Card> getHand() { return hand; }
    public Client getClient() { return client; }
    public Card getCardOnPile()	{ return cardOnPile;}
    public void setCardOnPile(Card cardOnPile) { this.cardOnPile = cardOnPile; }
    public void setMyTurn(boolean myTurn) { this.myTurn = myTurn; }
    public boolean getMyTurn() { return myTurn; }
    
    public PlayerData(Client client)
    {
        this.client = client;
        hand = new ArrayList<>();
        myTurn = false;
    }

}
