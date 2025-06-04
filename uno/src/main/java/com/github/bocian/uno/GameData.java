package com.github.bocian.uno;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    private Deck deck;
    private List<Card> discardPile = new ArrayList<>();
    private List<PlayerData> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private boolean isClockwise = true;

    public GameData() {
        // Możesz dodać graczy tutaj lub przez addPlayer()
    }

    public void generateNewDeck() {
        deck = new Deck();
        playStartCard();
    }

    private void playStartCard() {
        Card card = deck.drawCard();
        discardPile.add(card);
    }

    public Card drawCard() {
        if (!deck.canDraw()) swapPileWithDeck();
        return deck.drawCard();
    }

    private void swapPileWithDeck() {
        deck.generateDeckFromDiscardPile(discardPile);
        discardPile.clear();
        playStartCard();
    }

    public void throwCardOnPile(Card card) {
        discardPile.add(card);

        // Obsługa efektów specjalnych kart
        switch (card.getValue()) {
            case stop:
                nextTurn(); // pomija następnego gracza
                break;
            case swapTurn:
                reverseDirection(); // zmienia kierunek
                break;
            case plusTwo:
                nextTurn();
                getCurrentPlayer().drawCards(2, this);
                break;
            case plusFour:
                nextTurn();
                getCurrentPlayer().drawCards(4, this);
                break;
            default:
                break;
        }

        // Zmiana tury
        nextTurn();
    }

    public Card getCardOnPile() {
        return discardPile.getLast();
    }

    public PlayerData getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    public void reverseDirection() {
        isClockwise = !isClockwise;
    }

    public void addPlayer(PlayerData player) {
        players.add(player);
    }

    public List<PlayerData> getPlayers() {
        return players;
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
