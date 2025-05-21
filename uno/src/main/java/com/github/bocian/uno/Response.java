package com.github.bocian.uno;

import java.io.Serializable;

public class Response extends Payload
{
    private Boolean success;
    private Command command;

    
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
    
    public Command getCommand() { return command; }
    public Boolean isSucceed() { return success; }
    public static Response sendPileCard(Serializable card) { return new Response(card,Command.GET_PILE_CARD); }
    public static Response legalPlayedCard() { return new Response(Command.PLAY_CARD,true); }
    public static Response illegalPlayedCard() { return new Response(Command.PLAY_CARD,false); }
}
