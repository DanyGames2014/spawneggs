package net.danygames2014.spawneggs.init;

import net.danygames2014.spawneggs.ColorUtil;
import net.danygames2014.spawneggs.ColorizationHandler;
import net.danygames2014.spawneggs.ConfigHandler;
import net.danygames2014.spawneggs.SpawnEggs;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.client.event.render.model.PreLoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class TextureListener {

    public static JsonUnbakedModel eggModel;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        if (ConfigHandler.config.allowDevSword) {
            SpawnEggs.devSword.setTexture(NAMESPACE.id("item/dev_sword"));
        }
    }

    @EventListener
    public void registerModel(PreLoadUnbakedModelEvent event) {
        if (eggModel == null) {
            eggModel = JsonUnbakedModel.deserialize("{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"spawneggs:item/spawn_egg_outer\",\"layer1\":\"spawneggs:item/spawn_egg_inner\",\"layer2\":\"spawneggs:item/spawn_egg_inner_overlay\"}}");
        }

        if (event.identifier.namespace.equals(SpawnEggs.MOD_ID) && event.identifier.path.contains("spawn_egg")) {
            event.loader = identifier -> eggModel;
        }
    }

    @EventListener
    public void registerSpawnEggColors(ItemColorsRegisterEvent event) {
        for (SpawnEggItem item : SpawnEggs.spawnEggs) {
            // If no color has been registered for this entity, calculate it
            if (!ColorizationHandler.eggColor.containsKey(item.spawnedEntity)) {
                // Try to calculate the color from the texture
                if (!ConfigHandler.config.automaticEggColorization || !ColorUtil.calculateEggColorFromTexture(item)) {
                    // If the calculation from texture fails, or is no allowed, use the default color
                    ColorizationHandler.registerSpawnEggColorInt(item.spawnedEntity, ColorizationHandler.BASE_COLOR, ColorizationHandler.BASE_COLOR, ColorizationHandler.BASE_COLOR);
                }
            }

            event.itemColors.register((itemInstance, layer) -> {
                // Im aware this could error if an egg doesnt have colors registered, but since its called per frame checking harms the performance
                return ColorizationHandler.eggColor.get(item.spawnedEntity)[layer];
            }, item);
        }
    }
}
