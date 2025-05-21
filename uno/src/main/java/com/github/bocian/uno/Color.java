package com.github.bocian.uno;
import java.util.HashMap;
import java.util.Map;

public enum Color
{
    red(0),
    blue(1),
    green(2),
    yellow(3),
    noColor(4);

    private int value;
    private static Map map = new HashMap<>();

    private Color(int value) {
        this.value = value;
    }

    static {
        for (Color col : Color.values()) {
            map.put(col.value, col);
        }
    }

    public static Color valueOf(int val) {
        return (Color) map.get(val);
    }

    public int getValue() {
        return value;
    }
}
