package com.github.bocian.uno;

import java.util.HashMap;
import java.util.Map;

public enum Value
{
    zero(0),
    one(1),
    two(2),
    three(3),
    four(4),
    five(5),
    six(6),
    seven(7),
    eight(8),
    nine(9),
    plusTwo(10),
    stop(11),
    swapTurn(12),
    changeColor(13),
    plusFour(14);

    private int value;
    private static Map map = new HashMap<>();

    public static Value valueOf(int val) { return (Value) map.get(val); }
    public int getValue() { return value; }
    
    //konstruktor
    private Value(int value)
    { 
    	this.value = value;
    }

    static {
        for (Value val : Value.values()) {
            map.put(val.value, val);
        }
    }
}