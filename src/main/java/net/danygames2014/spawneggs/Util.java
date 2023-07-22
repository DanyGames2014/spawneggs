package net.danygames2014.spawneggs;

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
}
