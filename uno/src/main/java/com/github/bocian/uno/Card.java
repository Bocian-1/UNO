package com.github.bocian.uno;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Card implements Serializable
{
    Value value;
    Color color;

    //konstruktor
    public Card(Value value,Color color)
    {
        this.value = value;
        this.color = color;
    }

    @Override
    public String toString() { return color + "_" + value; }
    public Value getValue() { return value; }
    public void setColor(Color color) {
        this.color = color;
    }
    public static boolean canPlay(Card pileCard, Card playedCard)
    {
        if (playedCard.value == Value.wildCard || playedCard.value == Value.plusFour) {
            return true;
        }
        // Jeśli kolor lub wartość pasują – legalny ruch
        return pileCard.color == playedCard.color || pileCard.value == playedCard.value;
    }

}