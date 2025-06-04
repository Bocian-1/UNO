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
    public static Boolean canPlay(Card card1, Card card2)
    {
        return (card1.color == card2.color || card1.value == card2.value || card2.color == Color.noColor);
    }
}