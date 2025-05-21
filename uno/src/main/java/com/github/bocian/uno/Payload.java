package com.github.bocian.uno;

import java.io.Serializable;

public abstract class Payload implements Serializable
{
    protected Serializable payload;
    
    
    public Serializable getPayload() {return payload; }
}
