package com.github.bocian.uno;

import java.io.Serializable;

public class Card implements Serializable
{
    Value value;
    Color color;

    public Card(Value value,Color color)
    {
        this.value = value;
        this.color = color;

    }

    @Override
    public String toString()
    {
        return value + " " + color;
    }
    public Value getValue()
    {
        return value;
    }
    public static Boolean canPlay(Card card1, Card card2)
    {
        if(card1.color == card2.color || card1.value == card2.value || card2.color == Color.noColor)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}