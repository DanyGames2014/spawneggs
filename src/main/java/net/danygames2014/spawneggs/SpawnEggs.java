package net.danygames2014.spawneggs;

import net.danygames2014.spawneggs.api.event.SpawnEggColorizationEvent;
import net.danygames2014.spawneggs.item.DevSwordItem;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.danygames2014.spawneggs.mixin.EntityRegistryAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.resource.language.TranslationInvalidationEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class SpawnEggs {
    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    // Items
    public static Item devSword;
    public static ArrayList<SpawnEggItem> spawnEggs = new ArrayList<>();

    // Entity Registry
    public static List<String> entityRegistry;

    @EventListener(priority = ListenerPriority.LOWEST)
    public void registerItems(ItemRegistryEvent event) {
        // Registers the Dev Sword if allowed in configy
        if(ConfigHandler.config.allowDevSword){
            devSword = new DevSwordItem(MOD_ID.id("dev_sword")).setTranslationKey(MOD_ID, "dev_sword");
        }

        // Use Mixin to access the list of registered entities
        entityRegistry = EntityRegistryAccessor.getEntities().values().stream().sorted().toList();

        // Fetches the entity blacklist from config
        List<String> entityBlacklist = Arrays.stream(ConfigHandler.config.blacklistedEntities).toList();

        // Register Spawn Eggs
        for (String item : entityRegistry){
            // Check if the entity is present on blacklist
            if(!entityBlacklist.contains(item)){
                // If present register the Spawn Egg
                LOGGER.info("Adding Spawn Egg for " + item);
                spawnEggs.add(new SpawnEggItem(item, true));
            }else{
                // If not present do not register the spawn egg
                LOGGER.info("Entity " + item + " found on blacklist, not adding!");
            }
        }

        // Call the Spawn Egg Colorization Event
        StationAPI.EVENT_BUS.post(new SpawnEggColorizationEvent());
    }

    @EventListener
    public void localizeSpawnEggs(TranslationInvalidationEvent event){
        for(var egg : spawnEggs){
//            LOGGER.info("Localizing " + egg.spawnedEntity + " Spawn Egg");
            LocalizationHandler.registerSpawnEggLocalization(egg.spawnedEntity);
        }
    }

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void preInit(InitEvent event){
        FabricLoader.getInstance().getEntrypointContainers("spawneggs:event_bus", Object.class).forEach(EntrypointManager::setup);
    }


}
