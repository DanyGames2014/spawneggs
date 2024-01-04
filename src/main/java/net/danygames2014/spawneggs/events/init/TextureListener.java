package net.danygames2014.spawneggs.events.init;

import net.danygames2014.spawneggs.ColorizationHandler;
import net.danygames2014.spawneggs.ConfigHandler;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class TextureListener {

    public static JsonUnbakedModel eggModel = JsonUnbakedModel.deserialize("{\"parent\": \"item/generated\",\"textures\": {\"layer0\": \"spawneggs:item/spawn_egg_inner\",\"layer1\": \"spawneggs:item/spawn_egg_outer\"}}");

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        if(ConfigHandler.config.allowDevSword){
            ItemListener.devSword.setTexture(MOD_ID.id("item/dev_sword"));
        }

        for (SpawnEggItem item : ItemListener.spawnEggs){
            item.setTexture(MOD_ID.id("item/spawn_egg_default"));
        }
    }

    @EventListener
    public void registerSpawnEggColors(ItemColorsRegisterEvent event) {
        for (SpawnEggItem item : ItemListener.spawnEggs) {
            event.itemColors.register((itemInstance, layer) -> {
                // Im aware this could error if an egg doesnt have colors registered, but since its called per frame checking harms the performance
                return ColorizationHandler.eggColor.get(item.spawnedEntity)[layer];
            }, item);
        }
    }
}
