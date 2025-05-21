package com.github.bocian.uno;

import java.util.ArrayList;
import java.util.List;

public class GameData
{
    private Deck deck;
    List<Card> discardPile = new ArrayList<>();
    private List<Card> hands = new ArrayList<>();
    

    public void throwCardOnPile(Card card) { discardPile.add(card); }
    public Card getCardOnPile()	{ return discardPile.getLast(); }
    
    
    public GameData()
    {
    	//TODO
    }
    
    
    public void generateNewDeck()
    {
        deck = new Deck();
        playStartCard();
    }
    

    private void playStartCard()
    {
        Card card = deck.drawCard();
        discardPile.add(card);
    }
    
    
    public Card drawCard()
    {
        if(!deck.canDraw())
        {
            swapPileWithDeck();
        }
        return deck.drawCard();
    }
    
    
    private void swapPileWithDeck()
    {
        deck.generateDeckFromDiscardPile(discardPile);
        discardPile.clear();
        playStartCard();
    }
}
