package com.github.bocian.uno;

import java.util.List;
import java.util.Random;

public class Deck
{
    final static int DECKMAXSIZE = 108;
    int top;
    Card[] deck = new Card[DECKMAXSIZE];


    private void setTopAtStart() { top = DECKMAXSIZE-1; }
    public Boolean canDraw() { return top>0; }
    
    
    public Deck()
    {
        setTopAtStart();
        initCards();
        shuffleDeck();
        //printDeck();
    }
    
    
    private void initCards()
    {
        int head = 0;

        //tworzenie zer
        for(int i = 0;i<4;i++)
        {
            deck[head] = new Card(Value.valueOf(0),Color.valueOf(0+i));
            head++;
        }
        
        for(int i = 0;i<24;i++)
        {
            deck[head  ] = new Card( Value.valueOf(i/2+1),Color.red);
            deck[head+1] = new Card( Value.valueOf(i/2+1),Color.blue);
            deck[head+2] = new Card( Value.valueOf(i/2+1),Color.green);
            deck[head+3] = new Card( Value.valueOf(i/2+1),Color.yellow);
            head+=4;
        }
        
        for(int i = 0;i<8;i++)
        {
            deck[head] = new Card(Value.valueOf(i/4+Value.wildCard.getValue()),Color.noColor);
            head++;
        }
        
        //System.out.println(currentIndex);
    }
    
    public void generateDeckFromDiscardPile(List<Card> cards)
    {
        top = cards.size()-1;
        for (int i = 0;i<=top;i++)
        {
            deck[i] = cards.get(i);
        }
        shuffleDeck();
    }
    
    
    public void printDeck()
    {
        for(int i =0;i<=top;i++)
        {
            System.out.println(i+" "+deck[i].toString());
        }
    }
    
    
    public void shuffleDeck()
    {
        Random random = new Random();
        for (int i = top; i > 0; i--)
        {
            int randomIndex = random.nextInt(i + 1);
            Card temp = deck[i];
            deck[i] = deck[randomIndex];
            deck[randomIndex] = temp;

        }
    }
    
    
    public Card drawCard()
    {
        //handle
        Card card = deck[top];
        top--;
        return card;
    }
}
