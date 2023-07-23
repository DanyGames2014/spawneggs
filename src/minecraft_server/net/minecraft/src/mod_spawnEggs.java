package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class mod_spawnEggs extends BaseModMp{

    // Config
    @MLProp(name = "DevSwordItemID", info = "Item ID of the Dev Sword (Milos)")
    public static int devSwordItemID = 7629;
    @MLProp(name = "SpawnEggStartItemID", info = "Start of the spawn egg Item IDs, up to 128 Item IDS will be used and spawn egg ID will be (this value + Entity ID)")
    public static int spawnEggStartItemID = 7630;
    @MLProp(name = "ConsumeSpawnEgg", info = "If true, spawn egg will be consumed upon use")
    public static boolean consumeSpawnEgg = true;
    @MLProp(name = "AllowDevSword", info = "If true, the Dev Sword will be registered and available in-game. If false it will not be registered")
    public static boolean allowDevSword = true;
    @MLProp(name = "AllowSpawnInAir", info = "If true when right clicking and not aiming at a block, the entity will be spawned at player position")
    public static boolean allowSpawnInAir = false;
    @MLProp(name = "blacklistedEntityIDs", info = "Comma separated list of IDs to not allow spawn eggs for. The defaults only limit eggs which outright crash the game or don't work.")
    public static String blacklistedEntityIDs = "1,9,21,48";



    // Static Fields
    public static ArrayList<Integer> blacklistedID;
    public static Map classToStringMapping;
    public static Map IDtoClassMapping;

    public mod_spawnEggs() {
        System.out.printf("Initializing %s%n", Version());

        // Dev Sword
        if(allowDevSword){
            ItemDevSword DevSwordItem = (ItemDevSword) (new ItemDevSword(devSwordItemID,EnumToolMaterial.EMERALD)).setItemName("Miloš");
        }

        // Blacklist parsing
        String[] blacklistedIDString = blacklistedEntityIDs.split(",");
        blacklistedID = new ArrayList<Integer>();

        for (int i = 0; i < blacklistedIDString.length; i++) {
            blacklistedID.add(Integer.parseInt(blacklistedIDString[i]));
        }
    }

    @Override
    public void ModsLoaded() {
        // Create The Eggs
        String IDtoClassFieldName = ModLoader.isModLoaded("mod_spawnEggs") ? "c" : "IDtoClassMapping";
        String ClassToStringFieldName = ModLoader.isModLoaded("mod_spawnEggs") ? "b" : "classToStringMapping";

        Field IDToClassField;
        Field ClassToStringField;


        try{
            // Use Reflection to access the ID To Class Mapping
            IDToClassField = EntityList.class.getDeclaredField(IDtoClassFieldName);
            IDToClassField.setAccessible(true);
            IDtoClassMapping = (Map)IDToClassField.get(EntityList.class);

            ClassToStringField = EntityList.class.getDeclaredField(ClassToStringFieldName);
            ClassToStringField.setAccessible(true);
            classToStringMapping = (Map)ClassToStringField.get(EntityList.class);

            // Create the eggs from ID to class mapping
            for (int i = 0; i < 127; i++) {
                if (IDtoClassMapping.containsKey(i) && !blacklistedID.contains(i)) {
                    ItemSpawnEgg SpawnEggTemp = (ItemSpawnEgg) (new ItemSpawnEgg(spawnEggStartItemID + i, i)).setItemName(i + "SpawnEgg");
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String Version() {
        return "Spawn Eggs v1.3.0";
    }
}
