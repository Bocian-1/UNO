package com.github.bocian.uno;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Response extends Payload
{
    private Boolean success;
    private Command command;

    public Command getCommand() { return command; }
    public Boolean isSucceed() { return success; }
    public static Response sendPileCard(Serializable card) { return new Response(card,Command.GET_PILE_CARD); }
    public static Response startGame() {return new Response(Command.START_GAME,true);}
    public static Response legalPlayedCard() { return new Response(Command.PLAY_CARD,true); }
    public static Response illegalPlayedCard() { return new Response(Command.PLAY_CARD,false); }
    public static Response turnUpdate(Boolean isMyTurn) { return new Response(Command.TURN_UPDATE,isMyTurn); }

    public  Response(Command command,Boolean success)
    {
        this.success = success;
        this.command = command;
    }
    
    public  Response(Serializable payload,Command command)
    {
        this.payload = payload;
        this.command = command;
    }
}
