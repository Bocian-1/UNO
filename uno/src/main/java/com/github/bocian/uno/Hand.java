package com.github.bocian.uno;

import java.util.ArrayList;
import java.util.List;

public class Hand
{
    List<Card> hand = new ArrayList<>();
    public void drawCard(Card card)
    {
        hand.add(card);
    }
}
