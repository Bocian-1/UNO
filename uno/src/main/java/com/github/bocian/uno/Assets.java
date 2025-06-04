package com.github.bocian.uno;

/*
 * 19 Red cards – 0 to 9.
 * 19 Blue cards – 0 to 9.
 * 19 Green cards – 0 to 9.
 * 19 Yellow cards – 0 to 9.
 * 8 Skip cards – two cards of each color.
 * 8 Reverse cards – two cards of each color.
 * 8 Draw cards – two cards of each color.
 * 8 Black cards – 4 wild cards and 4 Wild Draw 4 cards.
 */

public enum Assets {
	RED_0, RED_1, RED_2, RED_3, RED_4, RED_5, RED_6, RED_7, RED_8, RED_9, RED_SKIP, RED_REVERSE, RED_DRAW,
	BLUE_0, BLUE_1, BLUE_2, BLUE_3, BLUE_4, BLUE_5, BLUE_6, BLUE_7, BLUE_8, BLUE_9, BLUE_SKIP, BLUE_REVERSE, BLUE_DRAW,
	GREEN_0, GREEN_1, GREEN_2, GREEN_3, GREEN_4, GREEN_5, GREEN_6, GREEN_7, GREEN_8, GREEN_9, GREEN_SKIP, GREEN_REVERSE, GREEN_DRAW,
	YELLOW_0, YELLOW_1, YELLOW_2, YELLOW_3, YELLOW_4, YELLOW_5, YELLOW_6, YELLOW_7, YELLOW_8, YELLOW_, YELLOW_SKIP, YELLOW_REVERSE, YELLOW_DRAW,
	WILD_COLOR, WILD_DRAW
}
