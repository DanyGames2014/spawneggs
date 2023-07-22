package net.danygames2014.spawneggs.events.init;

import net.danygames2014.spawneggs.ConfigHandler;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

public class TextureListener {

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        if(ConfigHandler.config.allowDevSword){
            ItemListener.devSword.setTexture(MOD_ID.id("item/dev_sword"));
        }

        for (SpawnEggItem item : ItemListener.spawnEggs){
            item.setTexture(MOD_ID.id("item/spawn_egg"));
        }
    }
}
