package com.github.bocian.uno;

import java.io.Serializable;

public enum Command implements Serializable
{
    START_GAME,
    SET_NAME,
    DRAW_CARD,
    PLAY_CARD,
    GET_PILE_CARD,
    TURN_UPDATE,
    NOT_YOUR_TURN
}
