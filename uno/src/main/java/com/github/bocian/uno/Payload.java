package com.github.bocian.uno;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Payload implements Serializable
{
    protected Serializable payload;
   
    
    public Serializable getPayload() {return payload; }
}
