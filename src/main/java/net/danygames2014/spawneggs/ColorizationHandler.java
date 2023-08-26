package net.danygames2014.spawneggs;

import java.util.HashMap;

/**
 * Handles the colors for Spawn Eggs
 */
public class ColorizationHandler {
    public static HashMap<String, int[]> eggColor = new HashMap<>();

    public static HashMap<String, int[]> defaultEggColors = new HashMap<>();

    static {
        defaultEggColors.put("Spider",new int[]{0x6b2020,0x000000,0xff0000});
        defaultEggColors.put("Creeper",new int[]{0x206b20,0x00dd00,0x000000});
        defaultEggColors.put("Chicken", new int[]{0xbbbbbb,0xcccccc,0xff0000});
        defaultEggColors.put("Cow", new int[]{0x1f140c,0x2b1c11,0x4a4a4a});
        defaultEggColors.put("Ghast", new int[]{0xeeeeee,0xffffff,0xffffff});
        defaultEggColors.put("Zombie", new int[]{0x005050,0x009a9a,0x6a8f45});
        defaultEggColors.put("Giant", new int[]{0x005050,0x009a9a,0x607050});
        defaultEggColors.put("Slime", new int[]{0x3d6e0c,0x81c240,0x86b357});
        defaultEggColors.put("Sheep", new int[]{0xc9adab,0xffffff,0xeb9d98});
        defaultEggColors.put("Wolf", new int[]{0xe0cfbc,0xffffff,0xe3c6a6});
        defaultEggColors.put("Pig", new int[]{0xa15c5c,0xf2b8b6,0xbf6867});
        defaultEggColors.put("Squid", new int[]{0x0e2020,0x2a4466,0x80aed1});
        defaultEggColors.put("PigZombie", new int[]{0x7aa385,0xf2b8b6,0x2b5c28});
        defaultEggColors.put("Skeleton", new int[]{0x8a8a8a,0xcccccc,0x202020});
    }

    public static final int BASE_COLOR = 16777215; // 0xFFFFFF

    /**
     * Registers custom color for a spawn egg using Java int color (Bit 0-7 is Blue, Bit 8-15 is Green, Bit 16-23 is Red)
     * These values can be 0 - 16777215
     * @param entity Registry name of the entity to register egg colors for
     * @param outerLayer Color of the Outer Layer (Border, Smiley Face)
     * @param innerLayer Color of the Inner Layer (The Base Layer in the Middle)
     * @param innerLayerOverlay Color of the Inner Overlay (Layer containing spots that are overlay ontop of the inner layer)
     * @return Whether the registration was succesfull
     */
    public static boolean registerSpawnEggColorInt(String entity, int outerLayer, int innerLayer, int innerLayerOverlay){
        outerLayer = Util.clamp(outerLayer,0,16777215);
        innerLayer = Util.clamp(innerLayer,0,16777215);
        innerLayerOverlay = Util.clamp(innerLayerOverlay,0,16777215);

        eggColor.put(entity, new int[]{outerLayer, innerLayer, innerLayerOverlay});
        return true;
    }

    /**
     * Registers custom color for a spawn egg using Hex Color representation
     * These values can be 0x000000 - 0xFFFFFF
     * @param entity Registry name of the entity to register egg colors for
     * @param outerLayer Color of the Outer Layer (Border, Smiley Face)
     * @param innerLayer Color of the Inner Layer (The Base Layer in the Middle)
     * @param innerLayerOverlay Color of the Inner Overlay (Layer containing spots that are overlay ontop of the inner layer)
     * @return Whether the registration was succesfull
     */
    public static boolean registerSpawnEggColorHex(String entity, int outerLayer, int innerLayer, int innerLayerOverlay){
        return registerSpawnEggColorInt(entity, Util.hexColorToInt(outerLayer), Util.hexColorToInt(innerLayer), Util.hexColorToInt(innerLayerOverlay));
    }
}
