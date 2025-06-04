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
	red_zero, red_one, red_two, red_three, red_four, red_five, red_six, red_seven, red_eight, red_nine, red_stop, red_swapTurn, red_plusTwo,
	blue_zero, blue_one, blue_two, blue_three, blue_four, blue_five, blue_six, blue_seven, blue_eight, blue_nine, blue_stop, blue_swapTurn, blue_plusTwo,
	green_zero, green_one, green_two, green_three, green_four, green_five, green_six, green_seven, green_eight, green_nine, green_stop, green_swapTurn, green_plusTwo,
	yellow_zero, yellow_one, yellow_two, yellow_three, yellow_four, yellow_five, yellow_six, yellow_seven, yellow_eight, yellow_, yellow_stop, yellow_swapTurn, yellow_plusTwo,
	noColor_wildCard, noColor_plusFour
}
