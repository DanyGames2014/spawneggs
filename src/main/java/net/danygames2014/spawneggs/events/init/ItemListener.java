package net.danygames2014.spawneggs.events.init;

import net.danygames2014.spawneggs.ConfigHandler;
import net.danygames2014.spawneggs.SpawnEggs;
import net.danygames2014.spawneggs.Util;
import net.danygames2014.spawneggs.item.DevSwordItem;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.danygames2014.spawneggs.mixin.EntityRegistryAccessor;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ItemListener {

    // Items
    public static ItemBase devSword;
    public static ArrayList<SpawnEggItem> spawnEggs = new ArrayList<>();

    // Translation Reflection Shenanigans
    public static TranslationStorage translationStorage;
    public static Properties translations;

    // Entity Registry
    public static List<String> entityRegistry;

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

    @EventListener(priority = ListenerPriority.LOWEST)
    public void registerItems(ItemRegistryEvent event) throws IllegalAccessException {
        translationStorage = TranslationStorage.getInstance();

        translations = (Properties) Util.getField(TranslationStorage.class, new String[]{"translations","field_1174","field_1364"}).get(translationStorage);

        if(ConfigHandler.config.allowDevSword){
            devSword = new DevSwordItem(MOD_ID.id("dev_sword")).setTranslationKey(MOD_ID, "dev_sword");
        }

        entityRegistry = EntityRegistryAccessor.getEntities().values().stream().sorted().toList();

        List<String> entityBlacklist = Arrays.stream(ConfigHandler.config.blacklistedEntities).toList();

        for (String item : entityRegistry){
            //System.out.println(item);
            if(!entityBlacklist.contains(item)){
                SpawnEggs.LOGGER.info("Adding Spawn Egg for " + item);
                spawnEggs.add(new SpawnEggItem(item));
            }else{
                SpawnEggs.LOGGER.info("Entity " + item + " found on blacklist, not adding!");
            }
        }
    }
}
