package net.danygames2014.spawneggs.item;

import net.danygames2014.spawneggs.ConfigHandler;
import net.danygames2014.spawneggs.SpawnEggs;
import net.danygames2014.spawneggs.events.init.ItemListener;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.util.Colours;

public class SpawnEggItem extends TemplateItemBase implements CustomTooltipProvider {

    public String spawnedEntity;

    public SpawnEggItem(String spawnedEntity) {
        super(SpawnEggs.MOD_ID.id(spawnedEntity.toLowerCase()+"_spawn_egg"));
        this.spawnedEntity = spawnedEntity;

        //System.out.println(ItemListener.translations.getProperty("item.spawneggs:spawn_egg.name")); // %s Spawn Egg
        //System.out.println(ItemListener.translations.getProperty("entity.spawneggs:" + spawnedEntity.toLowerCase() + ".name")); // Zombie

        if(ItemListener.translations.containsKey("entity.spawneggs:" + spawnedEntity.toLowerCase() + ".name")){
            ItemListener.translations.put(
                    // KEY
                    "item.spawneggs:" + spawnedEntity.toLowerCase() + "_spawn_egg.name",

                    // VALUE
                    String.format(
                            // Generic Spawn Egg Name
                            ItemListener.translations.getProperty("item.spawneggs:spawn_egg.name"),
                            // Entity Name
                            ItemListener.translations.getProperty("entity.spawneggs:" + spawnedEntity.toLowerCase() + ".name")
                    )
            );
        }else{
            if(ConfigHandler.config.attemptLocalization){
                ItemListener.translations.put(
                        // KEY
                        "item.spawneggs:" + spawnedEntity.toLowerCase() + "_spawn_egg.name",

                        // VALUE
                        String.format(
                                // Generic Spawn Egg Name
                                ItemListener.translations.getProperty("item.spawneggs:spawn_egg.name"),
                                // Entity Name
                                spawnedEntity
                        )
                );
            }
        }

        setTranslationKey(ItemListener.MOD_ID, spawnedEntity.toLowerCase() + "_spawn_egg");
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level world, int x, int y, int z, int side) {
        return spawnEntity(item, player, world, x, y, z, side);
    }

    @Override
    public ItemInstance use(ItemInstance item, Level world, PlayerBase player) {
        if(ConfigHandler.config.allowSpawnInAir){
            spawnEntity(item, player, world, player.x, player.y, player.z, 6);
        }
        return item;
    }


    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        return new String[]{
                originalTooltip,
                "Registry Name : " + spawnedEntity
        };
    }


    // Spawns the entity defined in spawnedEntity
    private boolean spawnEntity(ItemInstance item, PlayerBase player, Level level, double x, double y, double z, int side){
        if(level.isServerSide){
            return true;
        }

        try {

            // Create Entity
            EntityBase entity = EntityRegistry.create(spawnedEntity, level);

            // Determine coordinates according to block side
            switch (side){
                case 0: y-=entity.height; break; // BOTTOM (Y--)
                case 1: y++; x+=0.5; z+=0.5; break; // TOP (Y++)
                case 2: z-=0.5; x+=0.5; break; // SIDE (Z--)
                case 3: z+=1.5; x+=0.5; break; // SIDE (Z++)
                case 4: x-=0.5; z+=0.5; break; // SIDE (X--)
                case 5: x+=1.5; z+=0.5; break; // SIDE (X++)
                default: y++; break;
            }

            // Set the Entity position
            entity.setPosition(x,y,z);

            // Spawn the Entity
            level.spawnEntity(entity);

        } catch (Exception e){
            SpawnEggs.LOGGER.error("Error when spawning Entity! \n" + e.getMessage());
            player.sendMessage(Colours.RED + "Error when spawning the entity!");
            if(ConfigHandler.config.removeInvalidSpawnEggs){
                item.count = 0;
                player.sendMessage(Colours.RED + "Removed invalid Spawn Egg! You can change this in config");
            }
            return false;
        }

        if(ConfigHandler.config.consumeSpawnEgg){
            item.count--;
        }

        return true;
    }
}
