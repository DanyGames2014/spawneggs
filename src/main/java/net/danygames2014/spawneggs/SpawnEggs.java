package net.danygames2014.spawneggs;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.spawneggs.api.event.SpawnEggBlacklistEvent;
import net.danygames2014.spawneggs.api.event.SpawnEggColorizationEvent;
import net.danygames2014.spawneggs.item.DevSwordItem;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.resource.language.TranslationInvalidationEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SpawnEggs {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER;

    // Items
    public static Item devSword;
    public static ObjectArrayList<SpawnEggItem> spawnEggs = new ObjectArrayList<>();

    @EventListener(priority = ListenerPriority.LOWEST)
    public void registerItems(ItemRegistryEvent event) {
        // Registers the Dev Sword if allowed in configy
        if (ConfigHandler.config.allowDevSword) {
            devSword = new DevSwordItem(NAMESPACE.id("dev_sword")).setTranslationKey(NAMESPACE, "dev_sword");
        }

        // Pre-touch the EntityRegistry
        String ignored = EntityRegistry.class.getName();

        // Use Mixin to access the list of registered entities
        //noinspection unchecked
        List<String> entityRegistry = (List<String>) EntityRegistry.classToId.values().stream().toList();

        // Fetches the entity blacklist from config
        ObjectArrayList<String> entityBlacklist = new ObjectArrayList<>(ConfigHandler.config.blacklistedEntities);

        // Allow mods to blacklist entities
        StationAPI.EVENT_BUS.post(new SpawnEggBlacklistEvent(entityBlacklist));

        // Register Spawn Eggs
        for (String item : entityRegistry) {
            // Check if the entity is present on blacklist
            if (!entityBlacklist.contains(item)) {
                // If present register the Spawn Egg
                LOGGER.info("Adding Spawn Egg for {}", item);
                spawnEggs.add(new SpawnEggItem(item, true));
            } else {
                // If not present do not register the spawn egg
                LOGGER.info("Entity {} found on blacklist, not adding!", item);
            }
        }

        // Call the Spawn Egg Colorization Event
        StationAPI.EVENT_BUS.post(new SpawnEggColorizationEvent());
    }

    @EventListener
    public void localizeSpawnEggs(TranslationInvalidationEvent event) {
        for (var egg : spawnEggs) {
            LocalizationHandler.registerSpawnEggLocalization(egg.spawnedEntity);
        }
    }

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void preInit(InitEvent event) {
        FabricLoader.getInstance().getEntrypointContainers("spawneggs:event_bus", Object.class).forEach(EntrypointManager::setup);
    }
}
