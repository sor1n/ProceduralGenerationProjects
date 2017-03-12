package main;

import java.awt.*;

/**
 * Created by Sorin on 11/12/2016.
 */
public enum ColorWheel
{
    RED(255, 0, 0),
    RED_ORANGE(255, 64, 0),
    ORANGE(255, 128, 0),
    YELLOW_ORANGE(255, 192, 0),
    YELLOW(255, 255, 0),
    YELLOW_GREEN(128, 255, 0),
    GREEN(0, 255, 0),
    BLUE_GREEN(0, 255, 128),
    BLUE(0, 0, 255),
    BLUE_VIOLET(128, 0, 255),
    VIOLET(128, 0, 128),
    RED_VIOLET(255, 0, 128);

    public int red, green, blue;
    ColorWheel(int r, int g, int b)
    {
        red = r;
        green = g;
        blue = b;
    }

    public ColorWheel getComplementary(ColorWheel c)
    {
        return values()[c.ordinal() + 6 <= 11 ? c.ordinal() + 6 : c.ordinal() - 6];
    }
}
