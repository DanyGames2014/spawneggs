package net.danygames2014.spawneggs.events.init;

import net.danygames2014.spawneggs.ColorizationHandler;
import net.danygames2014.spawneggs.ConfigHandler;
import net.danygames2014.spawneggs.SpawnEggs;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.client.event.render.model.PreLoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Arrays;

public class TextureListener {

    public static JsonUnbakedModel eggModel = JsonUnbakedModel.deserialize("{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"spawneggs:item/spawn_egg_outer\",\"layer1\":\"spawneggs:item/spawn_egg_inner\",\"layer2\":\"spawneggs:item/spawn_egg_inner_overlay\"}}");

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        if (ConfigHandler.config.allowDevSword) {
            SpawnEggs.devSword.setTexture(MOD_ID.id("item/dev_sword"));
        }

        // TODO : Fix This
        for (SpawnEggItem item : SpawnEggs.spawnEggs) {
            Entity entity = EntityRegistry.create(item.spawnedEntity, null);
            String textureId;
            if ((textureId = entity.method_1314()) != null) {
                BufferedImage texture = TextureHelper.getTexture(textureId);

                // TODO : Extract into a Util method with BufferedImage as an input
                int[] pixels = new int[texture.getWidth() * texture.getHeight()];
                texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), pixels, 0, texture.getWidth());

                int lightestColorL = 0;
                Color lightestColor = null;

                int darkestColorL = 255;
                Color darkestColor = null;

                for (int colorInt : pixels) {
                    // Decode Colors
                    int A = (colorInt >> 24) & 255;
                    int R = (colorInt >> 16) & 255;
                    int G = (colorInt >> 8) & 255;
                    int B = (colorInt) & 255;

                    // Skip pixels with Alpha below 128
                    if (A < 128){
                        continue;
                    }

                    // Calculate "Lightness"
                    int L = (R + R + R + B + G + G + G + G) >> 3;

                    Color color = new Color(R, G, B);

                    //System.out.println("R : " + R + " | G : " + G + " | B : " + B + " | Lightness : " + L);

                    if (L > lightestColorL) {
                        lightestColorL = L;
                        lightestColor = color;
                    }

                    if (L < darkestColorL) {
                        darkestColorL = L;
                        darkestColor = color;
                    }
                }

                System.out.println("\n" + item.spawnedEntity);
                System.out.println("Lightest : " + lightestColor);
                System.out.println("Darkest : " + darkestColor);
                // End of method extract

                //ColorizationHandler.registerSpawnEggColorInt(item.spawnedEntity, Math.abs(lightestColor.getRGB()), Math.abs(darkestColor.getRGB()), Math.abs(lightestColor.getRGB()));

            }
        }
    }

    @EventListener
    public void registerModel(PreLoadUnbakedModelEvent event) {
        if (event.identifier.namespace.equals(SpawnEggs.MOD_ID) && event.identifier.path.contains("spawn_egg")) {
            event.loader = identifier -> eggModel;
        }
    }

    @EventListener
    public void registerSpawnEggColors(ItemColorsRegisterEvent event) {
        for (SpawnEggItem item : SpawnEggs.spawnEggs) {
            event.itemColors.register((itemInstance, layer) -> {
                // Im aware this could error if an egg doesnt have colors registered, but since its called per frame checking harms the performance
                return ColorizationHandler.eggColor.get(item.spawnedEntity)[layer];
            }, item);
        }
    }
}
