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
            deck[head] = new Card(Value.zero,Color.valueOf(0+i));
            head++;
        }
        
        //head = addCardType(24,Color.red,head);
        //head = addCardType(24,Color.blue,head);
        //head = addCardType(24,Color.green,head);
        //head = addCardType(24,Color.yellow,head);
        
        for(int i = 0;i<24;i++)
        {
            deck[head] = new Card( Value.valueOf(i/2+1),Color.red);
            head++;
        }
        
        for(int i = 0;i<24;i++)
        {
            deck[head] = new Card( Value.valueOf(i/2+1),Color.blue);
            head++;
        }
        
        for(int i = 0;i<24;i++)
        {
            deck[head] = new Card( Value.valueOf(i/2+1),Color.green);
            head++;
        }
        
        for(int i = 0;i<24;i++)
        {
            deck[head] = new Card( Value.valueOf(i/2+1),Color.yellow);
            head++;
        }
        
        for(int i = 0;i<8;i++)
        {
            deck[head] = new Card(Value.valueOf(i/4+Value.changeColor.getValue()),Color.noColor);
            head++;
        }
        
        //System.out.println(currentIndex);
    }
    
    
    private int addCardType (int count, Color color, int head) {
    	for(int i = 0;i<count;i++)
        {
            deck[head+i] = new Card( Value.valueOf(i/2+1),Color.green);
        }
    	return head+count;
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
