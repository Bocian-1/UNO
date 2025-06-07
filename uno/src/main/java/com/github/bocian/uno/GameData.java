package com.github.bocian.uno;

import java.util.ArrayList;
import java.util.List;

public class GameData
{
    private Deck deck;
    List<Card> discardPile = new ArrayList<>();
    private List<Card> hands = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private boolean turnsSwapped;


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
        if(!deck.canDraw()) swapPileWithDeck();
        return deck.drawCard();
    }


    private void swapPileWithDeck()
    {
        deck.generateDeckFromDiscardPile(discardPile);
        discardPile.clear();
        playStartCard();
    }
    public int getNextPlayer(int playerCount)
    {
        if(!turnsSwapped) return (currentPlayerIndex+1)%playerCount;
        return ((currentPlayerIndex-1)+playerCount)%playerCount;

    }
    public int getNextTurn(int playerCount)
    {
        if(!turnsSwapped)
        {
            currentPlayerIndex = (currentPlayerIndex+1)%playerCount;
            return currentPlayerIndex;
        }
        currentPlayerIndex = ((currentPlayerIndex-1)+playerCount)%playerCount;
        return currentPlayerIndex;
    }
    public void swapTurnDirections()
    {
        turnsSwapped = !turnsSwapped;
    }
}
