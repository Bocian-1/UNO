package com.github.bocian.uno;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Request extends Payload
{
    private Command command;

    
    public Command getCommand() { return command; }
    public static Request drawCard() { return new Request(Command.DRAW_CARD); }
    public static Request playCard(Serializable card) { return new Request(Command.PLAY_CARD,card); }
    public static Request setName() { return new Request(Command.SET_NAME); }
    private Request(Command command) { this.command = command; }
    
    private Request(Command command, Serializable payload)
    { 
    	this.command = command;
    	this.payload = payload;
    }
}