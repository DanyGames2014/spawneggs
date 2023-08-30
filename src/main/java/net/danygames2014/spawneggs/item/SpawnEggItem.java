package net.danygames2014.spawneggs.item;

import net.danygames2014.spawneggs.ColorizationHandler;
import net.danygames2014.spawneggs.ConfigHandler;
import net.danygames2014.spawneggs.LocalizationHandler;
import net.danygames2014.spawneggs.SpawnEggs;
import net.danygames2014.spawneggs.mixin.EntityRegistryAccessor;
import net.minecraft.block.MobSpawner;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.util.Colours;

public class SpawnEggItem extends TemplateItemBase implements CustomTooltipProvider {

    // Registry name of the spawned entity
    public String spawnedEntity;

    /**
     * Creates a new Spawn Egg Item using the supplied colors
     * @param spawnedEntity Registry Name of the spawned entity
     * @param outerLayerColorHex Outer Layer color in Hex
     * @param innerLayerColorHex Inner Layer color in Hex
     * @param innerLayerOverlayColorHex Inner Overlay Layer color in Hex
     */
    public SpawnEggItem(String spawnedEntity, int outerLayerColorHex, int innerLayerColorHex, int innerLayerOverlayColorHex) {
        this(spawnedEntity);

        ColorizationHandler.registerSpawnEggColorHex(spawnedEntity, outerLayerColorHex, innerLayerColorHex, innerLayerOverlayColorHex);
    }

    /**
     * Creates a new Spawn Egg Item with the default color
     * @param spawnedEntity Registry Name of the spawned entity
     */
    public SpawnEggItem(String spawnedEntity, boolean tryUsePredefinedColors){
        this(spawnedEntity);

        if(tryUsePredefinedColors){
            if(ColorizationHandler.defaultEggColors.containsKey(spawnedEntity)){
                int[] colors = ColorizationHandler.defaultEggColors.get(spawnedEntity);
                ColorizationHandler.registerSpawnEggColorHex(spawnedEntity,colors[0],colors[1],colors[2]);
                return;
            }
        }
        ColorizationHandler.registerSpawnEggColorInt(spawnedEntity,ColorizationHandler.BASE_COLOR,ColorizationHandler.BASE_COLOR,ColorizationHandler.BASE_COLOR);
    }

    public SpawnEggItem(String spawnedEntity){
        super(SpawnEggs.MOD_ID.id(spawnedEntity.toLowerCase()+"_spawn_egg"));
        this.spawnedEntity = spawnedEntity;

        LocalizationHandler.registerSpawnEggLocalization(spawnedEntity);

        setTranslationKey(SpawnEggs.MOD_ID, spawnedEntity.toLowerCase() + "_spawn_egg");
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level world, int x, int y, int z, int side) {
        Class<? extends EntityBase> entityClass = EntityRegistryAccessor.getStringToIdMap().get(this.spawnedEntity);
        if(!Living.class.isAssignableFrom(entityClass)){
            SpawnEggs.LOGGER.debug("This entity cannot be set as a spawner entity due to it not being a Living Entity");
            return true;
        }

        BlockState blockState = world.getBlockState(x,y,z);
        if(blockState.getBlock() instanceof MobSpawner){
            TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(x,y,z);
            spawner.setEntityId(this.spawnedEntity);
            return true;
        }

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
