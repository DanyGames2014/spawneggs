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

    public static int hexColorToInt(int hexColor) {
        Color color = new Color(hexColor);
        return (((color.getRed() & 255) << 16) | ((color.getGreen() & 255) << 8) | ((color.getBlue() & 255) << 0));
    }
}
