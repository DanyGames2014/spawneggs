package net.danygames2014.spawneggs.item;

import net.danygames2014.spawneggs.ColorizationHandler;
import net.danygames2014.spawneggs.ConfigHandler;
import net.danygames2014.spawneggs.SpawnEggs;
import net.danygames2014.spawneggs.mixin.EntityRegistryAccessor;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;

public class SpawnEggItem extends TemplateItem implements CustomTooltipProvider {

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

    public SpawnEggItem(String spawnedEntity) {
        super(SpawnEggs.MOD_ID.id(spawnedEntity.toLowerCase()+"_spawn_egg"));
        this.spawnedEntity = spawnedEntity;

        setTranslationKey(SpawnEggs.MOD_ID, spawnedEntity.toLowerCase() + "_spawn_egg");
    }

    @Override
    public boolean useOnBlock(ItemStack item, PlayerEntity player, World world, int x, int y, int z, int side) {
        Class<? extends Entity> entityClass = EntityRegistryAccessor.getStringToIdMap().get(this.spawnedEntity);

        BlockState blockState = world.getBlockState(x,y,z);
        if(blockState.getBlock() instanceof SpawnerBlock){
            if(!LivingEntity.class.isAssignableFrom(entityClass)){
                player.method_490("This entity cannot be set as a spawner entity due to it not being a Living Entity");
                SpawnEggs.LOGGER.info("This entity cannot be set as a spawner entity due to it not being a Living Entity");
                return true;
            }
            MobSpawnerBlockEntity spawner = (MobSpawnerBlockEntity) world.getBlockEntity(x,y,z);
            spawner.setSpawnedEntityId(this.spawnedEntity);
            return true;
        }

        return spawnEntity(item, player, world, x, y, z, side);
    }

    @Override
    public ItemStack use(ItemStack item, World world, PlayerEntity player) {
        if(ConfigHandler.config.allowSpawnInAir){
            spawnEntity(item, player, world, player.x, player.y, player.z, 6);
        }
        return item;
    }


    @Override
    public String[] getTooltip(ItemStack itemInstance, String originalTooltip) {
        return new String[]{
                originalTooltip,
                "Registry Name : " + spawnedEntity
        };
    }


    // Spawns the entity defined in spawnedEntity
    private boolean spawnEntity(ItemStack item, PlayerEntity player, World level, double x, double y, double z, int side){
        if(level.isRemote){
            return true;
        }

        try {
            // Create Entity
            Entity entity = EntityRegistry.create(spawnedEntity, level);

            // Determine coordinates according to block side
            switch (side){
                case 0: y-=entity.height; break; // BOTTOM (Y--)
                case 1: y+= (Math.max(entity.height, 0.5F) + 1F); x+=0.5; z+=0.5; break; // TOP (Y++)
                case 2: z-=0.5; x+=0.5; break; // SIDE (Z--)
                case 3: z+=1.5; x+=0.5; break; // SIDE (Z++)
                case 4: x-=0.5; z+=0.5; break; // SIDE (X--)
                case 5: x+=1.5; z+=0.5; break; // SIDE (X++)
                default: y++; break;
            }

            // Set the Entity position
            entity.setPos(x,y,z);

            // Spawn the Entity
            level.spawnEntity(entity);

        } catch (Exception e){
            SpawnEggs.LOGGER.error("Error when spawning Entity! \n" + e.getMessage());
            player.method_490(Formatting.RED + "Error when spawning the entity!");
            if(ConfigHandler.config.removeInvalidSpawnEggs){
                item.count = 0;
                player.method_490(Formatting.RED + "Removed invalid Spawn Egg! You can change this in config");
            }
            return false;
        }

        if(ConfigHandler.config.consumeSpawnEgg){
            item.count--;
        }

        return true;
    }
}
