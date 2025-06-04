package com.github.bocian.uno;

import java.util.ArrayList;
import java.util.List;
import com.github.bocian.uno.GameData;


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
    
    public PlayerData(Client client)
    {
        this.client = client;
        hand = new ArrayList<>();
        myTurn = false;
    }

    public void drawCards(int count, GameData gameData) {
        for (int i = 0; i < count; i++) {
            drawCard(gameData.drawCard());
        }
    }

}
