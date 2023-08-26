package net.danygames2014.spawneggs;

import java.awt.*;
import java.lang.reflect.Field;

public class Util {
    /**
     * Used for easy reflection with obfuscated or regular fields
     * @author mine_diver
     */
    public static final Field getField(Class<?> target, String names[]) {
        for (Field field : target.getDeclaredFields()) {
            for (String name : names) {
                if (field.getName() == name) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * Converts color represented in hex into color represented by int
     * @param hexColor Color in Hex (such as 0xFFFFFF)
     * @return The same color in an int representation
     */
    public static int hexColorToInt(int hexColor) {
        Color color = new Color(hexColor);
        return (((color.getRed() & 255) << 16) | ((color.getGreen() & 255) << 8) | ((color.getBlue() & 255) << 0));
    }


    public static int clamp(int value, int min, int max){
        if(value < min){return min;}
        if(value > max){return max;}
        return value;
    }
}
